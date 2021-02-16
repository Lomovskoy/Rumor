<style type="text/css">
    body {
        font-family: 'Capriola', sans-serif;
    }

    .wrap {
        margin: 0 auto;
        width: 1000px;
    }

    .logo h1 {
        font-size: 200px;
        color: #30e5fd;
        text-align: center;
        margin-bottom: 1px;
        text-shadow: 4px 4px 1px #ffffff;
    }

    .logo p {
        color: #B1A18D;
        font-size: 20px;
        margin-top: 1px;
        text-align: center;
    }

    .logo p span {
        color: #90ee90;
    }

    .sub a {
        color: #29b9fc;
        text-decoration: none;
        padding: 5px;
        font-size: 13px;
        font-family: arial, serif;
        font-weight: bold;
    }

    .footer a {
        color: #326bff;
    }

    @media (max-width: 1024px) {
        .logo h1 {
            font-size: 170px;
            margin-top: 140px
        }

        .wrap {
            width: 100%;
        }
    }

    @media (max-width: 991px) {
        .logo h1 {
            font-size: 150px;
        }
    }

    @media (max-width: 768px) {
        body {
            display: -webkit-flex;
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
            padding: 0;
            margin: 0;
        }

        .logo h1 {
            margin-top: 0px
        }
    }

    @media (max-width: 736px) {
        .logo h1 {
            font-size: 120px;
        }
    }

    @media (max-width: 600px) {
        .logo h1 {
            font-size: 100px;
        }
    }

    @media (max-width: 568px) {
        .logo p {
            font-size: 15px;
            margin-bottom: 5px;
            margin-top: 1px;
        }
    }

    @media (max-width: 480px) {
        .logo p {
            margin-bottom: 10px;
        }
    }

    @media (max-width: 320px) {
        .logo h1 {
            font-size: 90px;
        }

        .logo p {
            font-size: 14px;
            margin-top: 10px;
            margin-bottom: 15px;
        }
    }
</style>


<#import "parts/common.ftlh" as c>
<div class="wrap">
    <div class="logo">
        <h1>404</h1>
        <p> Простите - страница не найдена!</p>
        <div class="sub">
            <p><a href="/">Вернуться на главную</a></p>
        </div>
    </div>
</div>
