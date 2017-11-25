-- Add now strings
INSERT INTO ql_string (language_code, context, name, value, status) VALUES ('en', 'email-template', 'email-template-#listing-approved', '<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
    <body>
        <p>
            Dear <span th:text="${user.firstName}">${user.firstName}</span>
        </p>
        <p>
            Your listing <a th:text="${dlListing.title}" th:href="${listingUrl}">${dlListing.title}</a> has been approved.
        </p>
        <p>
            Thank you for beeing part of us.
        </p>
        <p>
            <span>Regards, </span>
            <br/>
            <em th:text="${baseName}">Quis Listing</em>
        </p>
    </body>
</html>', 0);
INSERT INTO ql_string (language_code, context, name, value, status) VALUES ('en', 'email-template', 'email-template-#listing-disapproved', '<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
    <body>
        <p>
            Dear <span th:text="${user.firstName}">${user.firstName}</span>
        </p>
        <p>
            Your listing <a th:text="${dlListing.title}" th:href="${previewListingUrl}">${dlListing.title}</a> has been disapproved.
        </p>
        <div th:if="${reason != null}">
            <p>
                <span th:text="${reason_label}">Reason: </span>
             </p>
             <div th:utext="${reason}">
                     - Not proper images </br>
                     - Not proper title
             </div>
         </div>
            <br/>
        <div>
             Follow this <a th:href="${editListingUrl}">link</a> to make a change to your listing.
        </div>
            <br/>
        <div>
            <span>Regards, </span>
            <br/>
            <em th:text="${baseName}">Quis Listing</em>
        </div>
    </body>
</html>', 0);

-- Add now translations

INSERT INTO ql_string_translation (string_id, language_code, status, value, translation_date) VALUES ((SELECT ID FROM ql_string s WHERE s.name='email-template-#listing-approved'), 'ro', 0, '<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
    <body>
        <p>
            Stimate <span th:text="${user.firstName}">${user.firstName}</span>
        </p>
        <p>
            Listarea <a th:text="${dlListing.title}" th:href="${listingUrl}">${dlListing.title}</a> a fost aprobată.
        </p>
        <p>
            Vă mulțumim că ați făcut parte din noi.
        </p>
        <p>
            <span>Salutari, </span>
            <br/>
            <em th:text="${baseName}">Quis Listing</em>
        </p>
    </body>
</html>', '2017-11-25 16:37:30');
INSERT INTO ql_string_translation (string_id, language_code, status, value, translation_date) VALUES ((SELECT ID FROM ql_string s WHERE s.name='email-template-#listing-approved'), 'bg', 0, '<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
    <body>
        <p>
            Мили <span th:text="${user.firstName}">${user.firstName}</span>
        </p>
        <p>
            Вашата обява <a th:text="${dlListing.title}" th:href="${listingUrl}">${dlListing.title}</a> е бил одобрен.
        </p>
        <p>
            Благодарим Ви, че бъдеш част от нас.
        </p>
        <p>
            <span>Поздрави, </span>
            <br/>
            <em th:text="${baseName}">Quis Listing</em>
        </p>
    </body>
</html>', '2017-11-25 16:37:30');
INSERT INTO ql_string_translation (string_id, language_code, status, value, translation_date) VALUES ((SELECT ID FROM ql_string s WHERE s.name='email-template-#listing-disapproved'), 'bg', 0, '<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
    <body>
        <p>
            Мили <span th:text="${user.firstName}">${user.firstName}</span>
        </p>
        <p>
            Вашата обява <a th:text="${dlListing.title}" th:href="${previewListingUrl}">${dlListing.title}</a> не е била одобрена.
        </p>
        <div th:if="${reason != null}">
            <p>
                <span th:text="${reason_label}">Reason: </span>
             </p>
             <div th:utext="${reason}">
                     - Not proper images </br>
                     - Not proper title
             </div>
         </div>
            <br/>
        <div>
             Следвайте тази <a th:href="${editListingUrl}">връзка</a> а да направите промяна в обявата си.
        </div>
            <br/>
        <div>
            <span>Поздрави, </span>
            <br/>
            <em th:text="${baseName}">Quis Listing</em>
        </div>
    </body>
</html>', '2017-11-25 16:37:41');
INSERT INTO ql_string_translation (string_id, language_code, status, value, translation_date) VALUES ((SELECT ID FROM ql_string s WHERE s.name='email-template-#listing-disapproved'), 'ro', 0, '<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
    <body>
        <p>
            Stimate <span th:text="${user.firstName}">${user.firstName}</span>
        </p>
        <p>
            Listarea <a th:text="${dlListing.title}" th:href="${previewListingUrl}">${dlListing.title}</a> a fost respins.
        </p>
        <div th:if="${reason != null}">
            <p>
                <span th:text="${reason_label}">Reason: </span>
             </p>
             <div th:utext="${reason}">
                     - Not proper images </br>
                     - Not proper title
             </div>
         </div>
            <br/>
        <div>
            Urmați acest <a th:href="${editListingUrl}">link</a> tpentru a modifica înregistrarea.
        </div>
            <br/>
        <div>
            <span>Salutari, </span>
            <br/>
            <em th:text="${baseName}">Quis Listing</em>
        </div>
    </body>
</html>', '2017-11-25 16:37:41');

-- Add now email template
INSERT INTO ql_email_template (string_id, name, text) VALUES ((SELECT ID FROM ql_string s WHERE s.name='email-template-#listing-approved'), 'listing-approved', '<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
    <body>
        <p>
            Dear <span th:text="${user.firstName}">${user.firstName}</span>
        </p>
        <p>
            Your listing <a th:text="${dlListing.title}" th:href="${listingUrl}">${dlListing.title}</a> has been approved.
        </p>
        <p>
            Thank you for beeing part of us.
        </p>
        <p>
            <span>Regards, </span>
            <br/>
            <em th:text="${baseName}">Quis Listing</em>
        </p>
    </body>
</html>');
INSERT INTO ql_email_template (string_id, name, text) VALUES ((SELECT ID FROM ql_string s WHERE s.name='email-template-#listing-disapproved'), 'listing-disapproved', '<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
    <body>
        <p>
            Dear <span th:text="${user.firstName}">${user.firstName}</span>
        </p>
        <p>
            Your listing <a th:text="${dlListing.title}" th:href="${previewListingUrl}">${dlListing.title}</a> has been disapproved.
        </p>
        <div th:if="${reason != null}">
            <p>
                <span th:text="${reason_label}">Reason: </span>
             </p>
             <div th:utext="${reason}">
                     - Not proper images </br>
                     - Not proper title
             </div>
         </div>
            <br/>
        <div>
             Follow this <a th:href="${editListingUrl}">link</a> to make a change to your listing.
        </div>
            <br/>
        <div>
            <span>Regards, </span>
            <br/>
            <em th:text="${baseName}">Quis Listing</em>
        </div>
    </body>
</html>');