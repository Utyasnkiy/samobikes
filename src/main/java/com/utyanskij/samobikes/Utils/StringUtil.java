package com.utyanskij.samobikes.Utils;

import com.utyanskij.samobikes.entities.Bike;

public final class StringUtil {
    //принимает направление сортировки (asc или desc) и возвращает противоположное направление сортировки.
    public static String reverseSortDir(String sortDir){
        return sortDir.equals("asc") ? "desc" : "asc";
    }


    // создает строку, представляющую тип события истории для объекта велосипеда.
    // Принимает объект велосипеда Bike и дополнительное сообщение msg, которое добавляется к типу события.
    public static String makeHistoryType(Bike bike, String msg){
        StringBuilder type = new StringBuilder();
        type
                .append("Велосипед ")
                .append(bike.getNumber())
                .append(" | ")
                .append(bike.getQrNumber())
                .append(" | ")
                .append(bike.getVIN())
                .append(msg);

        return type.toString();
    }
// создает строку, представляющую тип события истории для пользователя.
// Принимает имя пользователя username и дополнительное сообщение msg, которое добавляется к типу события.
    public static String makeHistoryType(String username, String msg){
        StringBuilder type = new StringBuilder();
        type
                .append("Пользователь ")
                .append(username)
                .append(msg);

        return type.toString();
    }


    //создает ссылку для перенаправления на страницу информации о велосипеде.
    public static String createBikePageRedirectLink(int id, String currentPage, String sortField,
                                              String sortDir, String commentSortDir, String keyword){
        StringBuilder redirectLink = new StringBuilder();

        redirectLink
                .append("redirect:/bikes/show/")
                .append(id)
                .append("?currentPage=")
                .append(currentPage)
                .append("&sortField=")
                .append(sortField)
                .append("&sortDir=")
                .append(sortDir)
                .append("&commentSortField=commentedAt&commentSortDir=")
                .append(commentSortDir)
                .append(keyword != null ? "&keyword=" + keyword : "");

        return redirectLink.toString();
    }
}
