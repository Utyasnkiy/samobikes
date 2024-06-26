package com.utyanskij.samobikes.services;

import com.utyanskij.samobikes.DTO.UserDTO;
import com.utyanskij.samobikes.repositories.RoleRepository;
import com.utyanskij.samobikes.repositories.UserRepository;
import com.utyanskij.samobikes.entities.Role;
import com.utyanskij.samobikes.entities.User;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
//представляет реализацию интерфейса UserService.
// Он предоставляет методы для работы с пользователями,

// такие как поиск пользователя по имени,
// загрузка пользователя по имени пользователя (необходимо для Spring Security),
// получение всех пользователей с пагинацией,
// удаление пользователя по идентификатору,
// проверка уникальности имени пользователя,
// сохранение и обновление учетной записи пользователя,
// регистрация новой учетной записи пользователя и управление активностью учетной записи.


//Этот сервис обеспечивает полное управление пользователями в приложении,
// включая аутентификацию и авторизацию с помощью Spring Security.
@Service
public class UserServiceImpl implements UserService{
    public static final int USERS_PER_PAGE = 6;
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setUserRepository (UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRoleRepository (RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User findByUserName(String username) {
        return userRepository.findOneByUsername(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findOneByUsername(username);
        if(user == null)
            throw new UsernameNotFoundException("Invalid username or password");
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    public User getById(Integer id){
        return userRepository.findById(id).get();
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    public Page<User> getAllByPage(int pageNum, String sortField, String sortDir, String keyword){
        Sort sort = Sort.by(sortField);
        if ("asc".equals(sortDir)){
            sort = sort.ascending();
        } else{
            sort = sort.descending();
        }

        Pageable pageable = PageRequest.of(pageNum - 1, USERS_PER_PAGE, sort);
        if (keyword != null){
            return userRepository.findAll(keyword, pageable);
        }
        return userRepository.findAll(pageable);
    }

    public List<Role> getAllRoles(){
        return roleRepository.findAll();
    }

    public void deleteById(Integer id){
        userRepository.deleteById(id);
    }

    public boolean isUsernameUnique(Integer id, String username){
        User user = userRepository.findOneByUsername(username);

        if (user == null)
            return true;
        else
            if (id == null)
                return false;
            else
                return user.getId().equals(id);
    }

    public void save(User user){
        if (user.getId() != null){
            User existingUser = userRepository.findById(user.getId()).get();
            if (user.getPassword().isEmpty()){
                user.setPassword(existingUser.getPassword());
            }else {
                encodePassword(user);
            }
        }else{
            encodePassword(user);
        }
        userRepository.save(user);
    }

    public void updateAccount(User oldUser){
        User newUser = userRepository.findOneByUsername(oldUser.getUsername());

        if (!oldUser.getPassword().isEmpty()) {
            newUser.setPassword(oldUser.getPassword());
            encodePassword(newUser);
        }

        newUser.setFirstName(oldUser.getFirstName());
        newUser.setLastName(oldUser.getLastName());

        userRepository.save(newUser);
    }

    private void encodePassword(User user){
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    @Override
    public void registerNewUserAccount(UserDTO userDTO){
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEnabled(true);

        Collection<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findOneByName("ROLE_USER"));
        user.setRoles(roles);

        user.setPassword(userDTO.getPassword());
        encodePassword(user);

        userRepository.save(user);
    }

    public void setEnabledById(Integer id, boolean enabled){
        userRepository.setEnabledById(id, enabled);
    }
}
