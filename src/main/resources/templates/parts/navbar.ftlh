<#include "security.ftlh">
<#import "login.ftlh" as l>

<nav class="navbar navbar-expand-lg fixed-top navbar-light bg-light">
    <a class="navbar-brand" href="/">Rumor</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
            aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <#if user??>
                <li class="nav-item">
                    <a class="nav-link" href="/main">Лента</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/user-messages/${currentUserId}">Моя страница</a>
                </li>
            </#if>
            <#if isAdmin>
                <li class="nav-item">
                    <a class="nav-link" href="/user">Список пользователей</a>
                </li>
            </#if>
            <#if user??>
                <li class="nav-item">
                    <a class="nav-link" href="/user/profile">Профиль</a>
                </li>
            </#if>
            <li class="nav-item">
                <a class="nav-link" href="/greeting">О нас</a>
            </li>
        </ul>

        <div class="navbar-text mr-3"><#if user??>${name}<#else>Пожалуйста, войдите</#if></div>
        <@l.logout />
    </div>
</nav>