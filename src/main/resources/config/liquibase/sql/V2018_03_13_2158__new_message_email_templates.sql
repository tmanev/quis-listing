-- Add now strings
INSERT INTO ql_string (language_code, context, name, value, status) VALUES ('en', 'email-template', 'email-template#you_have_new_message', '<table width="100%" border="0" cellspacing="0" cellpadding="0">
									    <tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												You have unread messages from <span th:text="${sender.firstName}">Trajche</span>
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												<span th:text="${sender.firstName}">Trajche</span> <span th:text="${sender.lastName}">Manev</span>
											</td>
										</tr>
										<tr bgcolor="F3F3F3">
											<td th:utext="${message}" style="padding: 10px; color: #000000; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Dear Mr. Trajche Manev,<br/><br/>
												Good Day.<br/>
												I have sent an email on your email id (sample.email@gmail.com). Please check your email and reply to me accordingly.<br/><br/>
												Kind Regards<br/>
												QuisListing
											</td>
										</tr>
									</table>
									<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top: 20px;">
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												<a th:href="@{|${conversationThreadLink}|}" href="#" style="padding: 4px 16px; color: #ffffff; white-space: normal; font-weight: 500; display: inline-block; text-decoration: none; border-color: #008cc9; background-color: #008cc9; border-radius: 2px; border-width: 1px; border-style: solid; margin-bottom: 4px;">
													Replay
												</a>
											</td>
										</tr>
									</table>
									<table th:if="${receiver.activated} == false" width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top: 20px;">
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												<span>To be able to reply to this message you have to be registered.</span>
												<a th:href="@{|${signUpLink}|}" href="https://google.com" >Sign up</a>
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">

											</td>
										</tr>
									</table>', 0);
-- Add now translations

INSERT INTO ql_string_translation (string_id, language_code, status, value, translation_date) VALUES ((SELECT ID FROM ql_string s WHERE s.name='email-template#you_have_new_message'), 'bg', 0, '<table width="100%" border="0" cellspacing="0" cellpadding="0">
									    <tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Имате непрочетени съобщения от <span th:text="${sender.firstName}">Trajche</span>
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												<span th:text="${sender.firstName}">Trajche</span> <span th:text="${sender.lastName}">Manev</span>
											</td>
										</tr>
										<tr bgcolor="F3F3F3">
											<td th:utext="${message}" style="padding: 10px; color: #000000; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Dear Mr. Trajche Manev,<br/><br/>
												Good Day.<br/>
												I have sent an email on your email id (sample.email@gmail.com). Please check your email and reply to me accordingly.<br/><br/>
												Kind Regards<br/>
												QuisListing
											</td>
										</tr>
									</table>
									<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top: 20px;">
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												<a th:href="@{|${conversationThreadLink}|}" href="#" style="padding: 4px 16px; color: #ffffff; white-space: normal; font-weight: 500; display: inline-block; text-decoration: none; border-color: #008cc9; background-color: #008cc9; border-radius: 2px; border-width: 1px; border-style: solid; margin-bottom: 4px;">
													Отговори
												</a>
											</td>
										</tr>
									</table>
									<table th:if="${receiver.activated} == false" width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top: 20px;">
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												<span>За да можете да отговорите на това съобщение, трябва да сте регистрирани.</span>
												<a th:href="@{|${signUpLink}|}" href="https://google.com" >Регистрирай се</a>
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">

											</td>
										</tr>
									</table>', '2017-11-25 16:37:30');
INSERT INTO ql_string_translation (string_id, language_code, status, value, translation_date) VALUES ((SELECT ID FROM ql_string s WHERE s.name='email-template#you_have_new_message'), 'ro', 0, '<table width="100%" border="0" cellspacing="0" cellpadding="0">
									    <tr>
											<td th:utext="${message}" style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Aveți mesaje necitite de la <span th:text="${sender.firstName}">Trajche</span>
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												<span th:text="${sender.firstName}">Trajche</span> <span th:text="${sender.lastName}">Manev</span>
											</td>
										</tr>
										<tr bgcolor="F3F3F3">
											<td style="padding: 10px; color: #000000; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Dear Mr. Trajche Manev,<br/><br/>
												Good Day.<br/>
												I have sent an email on your email id (sample.email@gmail.com). Please check your email and reply to me accordingly.<br/><br/>
												Kind Regards<br/>
												QuisListing
											</td>
										</tr>
									</table>
									<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top: 20px;">
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												<a th:href="@{|${conversationThreadLink}|}" href="#" style="padding: 4px 16px; color: #ffffff; white-space: normal; font-weight: 500; display: inline-block; text-decoration: none; border-color: #008cc9; background-color: #008cc9; border-radius: 2px; border-width: 1px; border-style: solid; margin-bottom: 4px;">
													Răspundeți
												</a>
											</td>
										</tr>
									</table>
									<table th:if="${receiver.activated} == false" width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top: 20px;">
									<table th:if="${receiver.activated} == false" width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top: 20px;">
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												<span>Pentru a putea răspunde la acest mesaj, trebuie să fiți înregistrat.</span>
												<a th:href="@{|${signUpLink}|}" href="https://google.com" >Inscrie-te</a>
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">

											</td>
										</tr>
									</table>', '2017-11-25 16:37:30');

-- Add now email template
INSERT INTO ql_email_template (string_id, name, text) VALUES ((SELECT ID FROM ql_string s WHERE s.name='email-template#you_have_new_message'), 'you_have_new_message', '<table width="100%" border="0" cellspacing="0" cellpadding="0">
									    <tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												You have unread messages from <span th:text="${sender.firstName}">Trajche</span>
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												<span th:text="${sender.firstName}">Trajche</span> <span th:text="${sender.lastName}">Manev</span>
											</td>
										</tr>
										<tr bgcolor="F3F3F3">
											<td th:utext="${message}" style="padding: 10px; color: #000000; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												Dear Mr. Trajche Manev,<br/><br/>
												Good Day.<br/>
												I have sent an email on your email id (sample.email@gmail.com). Please check your email and reply to me accordingly.<br/><br/>
												Kind Regards<br/>
												QuisListing
											</td>
										</tr>
									</table>
									<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top: 20px;">
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												<a th:href="@{|${conversationThreadLink}|}" href="#" style="padding: 4px 16px; color: #ffffff; white-space: normal; font-weight: 500; display: inline-block; text-decoration: none; border-color: #008cc9; background-color: #008cc9; border-radius: 2px; border-width: 1px; border-style: solid; margin-bottom: 4px;">
													Replay
												</a>
											</td>
										</tr>
									</table>
									<table th:if="${receiver.activated} == false" width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top: 20px;">
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">
												<span>To be able to reply to this message you have to be registered.</span>
												<a th:href="@{|${signUpLink}|}" href="https://google.com" >Sign up</a>
											</td>
										</tr>
										<tr>
											<td style="color: #3c4752; font-family: Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; padding-bottom: 16px;">

											</td>
										</tr>
									</table>');