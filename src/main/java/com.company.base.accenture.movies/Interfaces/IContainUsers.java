package com.company.base.accenture.movies.Interfaces;

public interface IContainUsers {
    public String registerUsers(String regName, String regLogin, String regPassword);
    public String registerUsers(String regName, String regLogin, String regPassword, String admin);
    public String loginOldUsers(String name, String login, String password);
    public String logout();

}
