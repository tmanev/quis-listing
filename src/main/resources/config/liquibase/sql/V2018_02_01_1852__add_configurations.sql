INSERT INTO ql_config (ql_key, value) VALUES ('support_email', 'contact@quislisting.com');

INSERT INTO ql_string (language_code, context, name, value, status) VALUES ('en', 'email-template', 'email-template#base_email_template', '<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="format-detection" content="telephone=no">
    <title th:text="${baseName}">QuisListing.com</title>
</head>
<body>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
    <tr>
        <td>
            <table border="0" cellpadding="0" cellspacing="0" width="100%" style="max-width: 528px; margin: 0 auto">
                <tr>
                    <td align="center" valign="top" style="padding:40px 24px">
                        <a target="_blank"
                           href="http://localhost:8181"
						   th:href="${baseUrl}"
                           title="QuisListing.com" style="color:#5E82A6">
                            <img alt="QuisListing.com"
                                 src="http://localhost:8181/resources/images/logo-ql.png"
								 th:src="${emailLogoImage}"
                                 width="50" height="50"
                                 style="display: block; font-family: Helvetica, Arial, sans-serif; color: #CB7ABA; font-size: 16px;"
                                 border="0">
                        </a>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td>
            <table border="0" cellpadding="0" cellspacing="0" width="100%" style="max-width:528px; margin: 0 auto">
                <tr>
                    <td style="padding:0 24px 40px 24px">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td th:utext="${innerEmail}">

                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td align="center">
            <table border="0" cellpadding="0" cellspacing="0" width="100%" style="max-width: 800px; margin: 0 auto">
                <tr>
                    <td align="center" style="border-top:2px solid #F1F4F8;padding:24px">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td align="center" style="color:#3c4752;font-family:Helvetica,Arial,sans-serif;font-size:13px;font-weight:300;line-height:1.5;padding:0 0 10px 0">
                                    &copy;<span th:text="${currentYear}">2017</span> <span th:text="${baseName}">QuisListing.com</span>. All rights reserved.<br>
                                </td>
                            </tr>
                            <tr>
                                <td align="center" style="color:#3c4752;font-family:Helvetica,Arial,sans-serif;font-size:13px;font-weight:300;line-height:1.5;padding:0 0 10px 0">
                                    You are receiving this email because you are a registered member of our community <span th:text="${baseName}">QuisListing.com</span>.<br>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</body>
</html>', 0);
INSERT INTO ql_string_translation (string_id, language_code, status, value, translation_date) VALUES ((SELECT ID FROM ql_string s WHERE s.name='email-template#base_email_template'), 'bg', 0, '<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="format-detection" content="telephone=no">
    <title th:text="${baseName}">QuisListing.com</title>
</head>
<body>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
    <tr>
        <td>
            <table border="0" cellpadding="0" cellspacing="0" width="100%" style="max-width: 528px; margin: 0 auto">
                <tr>
                    <td align="center" valign="top" style="padding:40px 24px">
                        <a target="_blank"
                           href="http://localhost:8181"
						   th:href="${baseUrl}"
                           title="QuisListing.com" style="color:#5E82A6">
                            <img alt="QuisListing.com"
                                 src="http://localhost:8181/resources/images/logo-ql.png"
								 th:src="${emailLogoImage}"
                                 width="50" height="50"
                                 style="display: block; font-family: Helvetica, Arial, sans-serif; color: #CB7ABA; font-size: 16px;"
                                 border="0">
                        </a>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td>
            <table border="0" cellpadding="0" cellspacing="0" width="100%" style="max-width:528px; margin: 0 auto">
                <tr>
                    <td style="padding:0 24px 40px 24px">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td th:utext="${innerEmail}">

                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td align="center">
            <table border="0" cellpadding="0" cellspacing="0" width="100%" style="max-width: 800px; margin: 0 auto">
                <tr>
                    <td align="center" style="border-top:2px solid #F1F4F8;padding:24px">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td align="center" style="color:#3c4752;font-family:Helvetica,Arial,sans-serif;font-size:13px;font-weight:300;line-height:1.5;padding:0 0 10px 0">
                                    &copy;<span th:text="${currentYear}">2017</span> <span th:text="${baseName}">QuisListing.com</span>. Всички права запазени.<br>
                                </td>
                            </tr>
                            <tr>
                                <td align="center" style="color:#3c4752;font-family:Helvetica,Arial,sans-serif;font-size:13px;font-weight:300;line-height:1.5;padding:0 0 10px 0">
                                    Получавате този имейл, защото сте регистриран член на нашата общност <span th:text="${baseName}">QuisListing.com</span>.<br>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</body>
</html>', '2017-11-25 16:37:30');
INSERT INTO ql_string_translation (string_id, language_code, status, value, translation_date) VALUES ((SELECT ID FROM ql_string s WHERE s.name='email-template#base_email_template'), 'ro', 0, '<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="format-detection" content="telephone=no">
    <title th:text="${baseName}">QuisListing.com</title>
</head>
<body>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
    <tr>
        <td>
            <table border="0" cellpadding="0" cellspacing="0" width="100%" style="max-width: 528px; margin: 0 auto">
                <tr>
                    <td align="center" valign="top" style="padding:40px 24px">
                        <a target="_blank"
                           href="http://localhost:8181"
						   th:href="${baseUrl}"
                           title="QuisListing.com" style="color:#5E82A6">
                            <img alt="QuisListing.com"
                                 src="http://localhost:8181/resources/images/logo-ql.png"
								 th:src="${emailLogoImage}"
                                 width="50" height="50"
                                 style="display: block; font-family: Helvetica, Arial, sans-serif; color: #CB7ABA; font-size: 16px;"
                                 border="0">
                        </a>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td>
            <table border="0" cellpadding="0" cellspacing="0" width="100%" style="max-width:528px; margin: 0 auto">
                <tr>
                    <td style="padding:0 24px 40px 24px">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td th:utext="${innerEmail}">

                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td align="center">
            <table border="0" cellpadding="0" cellspacing="0" width="100%" style="max-width: 800px; margin: 0 auto">
                <tr>
                    <td align="center" style="border-top:2px solid #F1F4F8;padding:24px">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td align="center" style="color:#3c4752;font-family:Helvetica,Arial,sans-serif;font-size:13px;font-weight:300;line-height:1.5;padding:0 0 10px 0">
                                    &copy;<span th:text="${currentYear}">2017</span> <span th:text="${baseName}">QuisListing.com</span>. Toate drepturile rezervate.<br>
                                </td>
                            </tr>
                            <tr>
                                <td align="center" style="color:#3c4752;font-family:Helvetica,Arial,sans-serif;font-size:13px;font-weight:300;line-height:1.5;padding:0 0 10px 0">
                                    Primiți acest e-mail deoarece sunteți membru înregistrat al comunității noastre <span th:text="${baseName}">QuisListing.com</span>.<br>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</body>
</html>', '2017-11-25 16:37:30');
INSERT INTO ql_email_template (string_id, name, text) VALUES ((SELECT ID FROM ql_string s WHERE s.name='email-template#base_email_template'), 'base_email_template', '<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="format-detection" content="telephone=no">
    <title th:text="${baseName}">QuisListing.com</title>
</head>
<body>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
    <tr>
        <td>
            <table border="0" cellpadding="0" cellspacing="0" width="100%" style="max-width: 528px; margin: 0 auto">
                <tr>
                    <td align="center" valign="top" style="padding:40px 24px">
                        <a target="_blank"
                           href="http://localhost:8181"
						   th:href="${baseUrl}"
                           title="QuisListing.com" style="color:#5E82A6">
                            <img alt="QuisListing.com"
                                 src="http://localhost:8181/resources/images/logo-ql.png"
								 th:src="${emailLogoImage}"
                                 width="50" height="50"
                                 style="display: block; font-family: Helvetica, Arial, sans-serif; color: #CB7ABA; font-size: 16px;"
                                 border="0">
                        </a>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td>
            <table border="0" cellpadding="0" cellspacing="0" width="100%" style="max-width:528px; margin: 0 auto">
                <tr>
                    <td style="padding:0 24px 40px 24px">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td th:utext="${innerEmail}">

                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td align="center">
            <table border="0" cellpadding="0" cellspacing="0" width="100%" style="max-width: 800px; margin: 0 auto">
                <tr>
                    <td align="center" style="border-top:2px solid #F1F4F8;padding:24px">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td align="center" style="color:#3c4752;font-family:Helvetica,Arial,sans-serif;font-size:13px;font-weight:300;line-height:1.5;padding:0 0 10px 0">
                                    &copy;<span th:text="${currentYear}">2017</span> <span th:text="${baseName}">QuisListing.com</span>. All rights reserved.<br>
                                </td>
                            </tr>
                            <tr>
                                <td align="center" style="color:#3c4752;font-family:Helvetica,Arial,sans-serif;font-size:13px;font-weight:300;line-height:1.5;padding:0 0 10px 0">
                                    You are receiving this email because you are a registered member of our community <span th:text="${baseName}">QuisListing.com</span>.<br>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</body>
</html>');

UPDATE ql_email_template set text = '<table width="100%" border="0" cellspacing="0" cellpadding="0">
									    <tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Hi <span th:text="${user.firstName}">Trajche</span>,
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Thanks for signing up for <span th:text="${baseName}">QuisListing.com</span>!
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Before you start adding listings, please verify your email address to confirm your account.
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Why you ask? We''re verifying ownership of this email address to make sure it''s really you. And, if you ever lose your password, your email is the best way to recover it.
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												If you did not create an account on <span th:text="${baseName}">QuisListing.com</span> using this address, please contact us at <a th:text="${supportEmail}" th:href="''mailto:'' + ${supportEmail}" href="mailto:support@quislisting.com" style="color: #5E82A6;">support@quislisting.com</a>.
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Follow the link below to verify your account:
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												<a th:href="@{|${activationLink}|}" th:utext="${activationLink}">Activation Link</a>
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												If you cannot click the link above, please copy and paste it into your browser.
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Thanks,</br>
												<span th:text="${baseName}">QuisListing.com</span> Team,<br>
												<a th:text="${supportEmail}" th:href="''mailto:'' + ${supportEmail}" href="mailto:support@quislisting.com" style="color: #5E82A6;">support@quislisting.com</a>
											</td>
										</tr>
									</table>' WHERE name = 'activation_email';
UPDATE ql_string s SET s.value = '<table width="100%" border="0" cellspacing="0" cellpadding="0">
									    <tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Hi <span th:text="${user.firstName}">Trajche</span>,
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Thanks for signing up for <span th:text="${baseName}">QuisListing.com</span>!
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Before you start adding listings, please verify your email address to confirm your account.
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Why you ask? We''re verifying ownership of this email address to make sure it''s really you. And, if you ever lose your password, your email is the best way to recover it.
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												If you did not create an account on <span th:text="${baseName}">QuisListing.com</span> using this address, please contact us at <a th:text="${supportEmail}" th:href="''mailto:'' + ${supportEmail}" href="mailto:support@quislisting.com" style="color: #5E82A6;">support@quislisting.com</a>.
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Follow the link below to verify your account:
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												<a th:href="@{|${activationLink}|}" th:utext="${activationLink}">Activation Link</a>
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												If you cannot click the link above, please copy and paste it into your browser.
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Thanks,</br>
												<span th:text="${baseName}">QuisListing.com</span> Team,<br>
												<a th:text="${supportEmail}" th:href="''mailto:'' + ${supportEmail}" href="mailto:support@quislisting.com" style="color: #5E82A6;">support@quislisting.com</a>
											</td>
										</tr>
									</table>' WHERE s.id = (SELECT et.string_id FROM ql_email_template et WHERE et.name='activation_email') AND s.language_code = 'en';
UPDATE ql_string_translation st SET st.value = '<table width="100%" border="0" cellspacing="0" cellpadding="0">
									    <tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Здрасти <span th:text="${user.firstName}">Trajche</span>,
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Благодарим ви, че се регистрирахте <span th:text="${baseName}">QuisListing.com</span>!
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Преди да започнете да добавяте обяви, моля, потвърдете имейл адреса си, за да потвърдите профила си.
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Защо питаш? Осъществяваме проверка на собствеността върху този имейл адрес, за да сме сигурни, че наистина сте вие. И ако някога загубите паролата си, имейлът ви е най-добрият начин да я възстановите.
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Ако не сте създали профил на <span th:text="${baseName}">QuisListing.com</span> с този адрес, моля свържете се с нас на адрес <a th:text="${supportEmail}" th:href="''mailto:'' + ${supportEmail}" href="mailto:support@quislisting.com" style="color: #5E82A6;">support@quislisting.com</a>.
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Следвайте връзката по-долу, за да потвърдите профила си:
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												<a th:href="@{|${activationLink}|}" th:utext="${activationLink}">Activation Link</a>
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Ако не можете да кликнете върху връзката по-горе, моля, копирайте я и го поставете в браузъра си.
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Благодаря,</br>
												<span th:text="${baseName}">QuisListing.com</span> екип,<br>
												<a th:text="${supportEmail}" th:href="''mailto:'' + ${supportEmail}" href="mailto:support@quislisting.com" style="color: #5E82A6;">support@quislisting.com</a>
											</td>
										</tr>
									</table>' WHERE st.string_id = (SELECT et.string_id FROM ql_email_template et WHERE et.name='activation_email') AND st.language_code = 'bg';
UPDATE ql_string_translation st SET st.value = '<table width="100%" border="0" cellspacing="0" cellpadding="0">
									    <tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Bună <span th:text="${user.firstName}">Trajche</span>,
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Vă mulțumim că v-ați înscris <span th:text="${baseName}">QuisListing.com</span>!
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Înainte de a începe să adăugați anunțuri, verificați adresa dvs. de e-mail pentru a vă verifica contul.
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												De ce întrebi? Verificăm dreptul de proprietate asupra acestei adrese de e-mail pentru a vă asigura că sunteți cu adevărat. Și dacă vă pierdeți parola, e-mailul dvs. este cea mai bună modalitate de ao recupera.
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Dacă nu ați creat un cont <span th:text="${baseName}">QuisListing.com</span> cu această adresă, vă rugăm să ne contactați la <a th:text="${supportEmail}" th:href="''mailto:'' + ${supportEmail}support@quislisting.com" href="mailto:support@quislisting.com" style="color: #5E82A6;">support@quislisting.com</a>.
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Urmați linkul de mai jos pentru a vă verifica contul:
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												<a th:href="@{|${activationLink}|}" th:utext="${activationLink}">Activation Link</a>
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Dacă nu puteți face clic pe linkul de mai sus, copiați-l și inserați-l în browser.
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Mulțumesc,</br>
												<span th:text="${baseName}">QuisListing.com</span> echipă,<br>
												<a th:text="${supportEmail}" th:href="''mailto:'' + ${supportEmail}" href="mailto:support@quislisting.com" style="color: #5E82A6;">support@quislisting.com</a>
											</td>
										</tr>
									</table>' WHERE st.string_id = (SELECT et.string_id FROM ql_email_template et WHERE et.name='activation_email') AND st.language_code = 'ro';


UPDATE ql_email_template set text = '<table width="100%" border="0" cellspacing="0" cellpadding="0">
									    <tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Dear <span th:text="${user.firstName}">Trajche</span>,
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Someone requested that the password is reset to the following account:
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Username: <span th:text="${user.login}">trajche.manev@gmail.com</span>
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Follow the link below to reset your account:
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												<a th:href="@{|${resetLink}|}" th:utext="${resetLink}" href="http://localhost:8181/">Reset link</a>
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												If you cannot click the link above, please copy and paste it into your browser.
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Thanks,</br>
												<span th:text="${baseName}">QuisListing.com</span> Team,<br>
												<a th:text="${supportEmail}" th:href="''mailto:'' + ${supportEmail}" href="mailto:support@quislisting.com" style="color: #5E82A6;">support@quislisting.com</a>
											</td>
										</tr>
									</table>' WHERE name = 'password-reset';
UPDATE ql_string s SET s.value = '<table width="100%" border="0" cellspacing="0" cellpadding="0">
									    <tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Dear <span th:text="${user.firstName}">Trajche</span>,
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Someone requested that the password is reset to the following account:
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Username: <span th:text="${user.login}">trajche.manev@gmail.com</span>
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Follow the link below to reset your account:
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												<a th:href="@{|${resetLink}|}" th:utext="${resetLink}" href="http://localhost:8181/">Reset link</a>
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												If you cannot click the link above, please copy and paste it into your browser.
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Thanks,</br>
												<span th:text="${baseName}">QuisListing.com</span> Team,<br>
												<a th:text="${supportEmail}" th:href="''mailto:'' + ${supportEmail}" href="mailto:support@quislisting.com" style="color: #5E82A6;">support@quislisting.com</a>
											</td>
										</tr>
									</table>' WHERE s.id = (SELECT et.string_id FROM ql_email_template et WHERE et.name='password-reset') AND s.language_code = 'en';
UPDATE ql_string_translation st SET st.value = '<table width="100%" border="0" cellspacing="0" cellpadding="0">
									    <tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Уважаеми <span th:text="${user.firstName}">Trajche</span>,
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Някой поиска паролата да се върне към следния профил:
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Потребител: <span th:text="${user.login}">trajche.manev@gmail.com</span>
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Следвайте връзката по-долу, за да възстановите профила си:
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												<a th:href="@{|${resetLink}|}" th:utext="${resetLink}" href="http://localhost:8181/">Reset link</a>
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Ако не можете да кликнете върху връзката по-горе, моля, копирайте я и го поставете в браузъра си.
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Благодаря,</br>
												<span th:text="${baseName}">QuisListing.com</span> екип,<br>
												<a th:text="${supportEmail}" th:href="''mailto:'' + ${supportEmail}" href="mailto:support@quislisting.com" style="color: #5E82A6;">support@quislisting.com</a>
											</td>
										</tr>
									</table>' WHERE st.string_id = (SELECT et.string_id FROM ql_email_template et WHERE et.name='password-reset') AND st.language_code = 'bg';
UPDATE ql_string_translation st SET st.value = '<table width="100%" border="0" cellspacing="0" cellpadding="0">
									    <tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Dragă <span th:text="${user.firstName}">Trajche</span>,
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Cineva a cerut ca parola să fie resetată la următorul cont:
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Nume de utilizator: <span th:text="${user.login}">trajche.manev@gmail.com</span>
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Urmați link-ul de mai jos pentru a vă reseta contul:
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												<a th:href="@{|${resetLink}|}" th:utext="${resetLink}" href="http://localhost:8181/">Reset link</a>
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Dacă nu puteți face clic pe linkul de mai sus, copiați-l și inserați-l în browser.
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Mulțumiri,</br>
												<span th:text="${baseName}">QuisListing.com</span> echipă,<br>
												<a th:text="${supportEmail}" th:href="''mailto:'' + ${supportEmail}" href="mailto:support@quislisting.com" style="color: #5E82A6;">support@quislisting.com</a>
											</td>
										</tr>
									</table>' WHERE st.string_id = (SELECT et.string_id FROM ql_email_template et WHERE et.name='password-reset') AND st.language_code = 'ro';
