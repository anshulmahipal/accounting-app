package util;

import util.Encryptor;
import controller.screenController.BaseScreenController;
import javafx.scene.control.TextInputControl;
import main.App;


public class Validator
{
    private BaseScreenController controller;


    public Validator(BaseScreenController controller)
    {
        this.controller = controller;
    };

    public boolean verifyEmail(TextInputControl email)
    {
        if(email.getText().matches("[\\w.]+((@mail.ru)|(@gmail.com)){1}"))
            return true;
        else
        {
            controller.showErrorNotification("Недопустимый email адрес.");
            controller.indicateError(email);
            return false;
        }
    }

    public boolean verifyPassword(TextInputControl password)
    {
        if(password.getText().matches("\\S{8,}"))
            return true;
        else
        {
            controller.showErrorNotification("Пароль должен содержать не менее 8 символов.");
            controller.indicateError(password);
            return false;
        }
    }

    public boolean verifyLettersOnly(TextInputControl lettersOnly)
    {
        if(lettersOnly.getText().isEmpty() ||  lettersOnly.getText().matches("(([a-z]|[A-Z])|([а-я]|[А-Я])){3,}"))
            return true;
        else
        {
            controller.showErrorNotification("Недопустимое значение поля "+lettersOnly.getPromptText());
            controller.indicateError(lettersOnly);
            return false;
        }
    }

    public boolean verifyWorkplace(TextInputControl workplace)
    {
        if(workplace.getText().isEmpty())
            return true;
        return verifyContractorName(workplace);
    }

    public boolean verifyPhone(TextInputControl phone)
    {
        if(phone.getText().isEmpty())
            return true;
        return verifyContractorPhone(phone);
    }

    public boolean verifyPasswordEquality(TextInputControl password1, TextInputControl password2)
    {
        if(password1.getText().equals(password2.getText()))
            return true;
        else
        {
            controller.showErrorNotification("Пароли не совпадают.");
            controller.indicateError(password1);
            controller.indicateError(password2);
            return false;
        }
    }

    public boolean verifyAdminCode(TextInputControl adminCode)
    {
        if(adminCode.getText().equals(App.getAdminCode()))
            return true;
        else
        {
            controller.showErrorNotification("Неверный код администратора.");
            controller.indicateError(adminCode);
            return false;
        }
    }

    public boolean verifyProductName(TextInputControl productName)
    {
        if(!productName.getText().matches("\\d+") && !productName.getText().isEmpty())
            return true;
        else
        {
            controller.showErrorNotification("Недопустимое имя товара.");
            controller.indicateError(productName);
            return false;
        }
    }

    public boolean verifyDouble(TextInputControl onlyDigits)
    {
        if(onlyDigits.getText().matches("\\d+\\.?\\d+"))
            return true;
        else
        {
            controller.showErrorNotification("Недопустимое значение цены.");
            controller.indicateError(onlyDigits);
            return false;
        }
    }

    public boolean verifyContractorPhone(TextInputControl phone)
    {
        if(phone.getText().matches("((29)|(33)|(17)){1}\\d{7}"))
            return true;
        else
        {
            controller.showErrorNotification("Неверный номер телефона.");
            controller.indicateError(phone);
            return false;
        }
    }

    public boolean verifyRequisites(TextInputControl requisites)
    {
        if(requisites.getText().matches("р/с\\s\\d{13}"))
            return true;
        else
        {
            controller.showErrorNotification("Недопустимое значение реквизита.");
            controller.indicateError(requisites);
            return false;
        }
    }

    public boolean verifyContractorName(TextInputControl name)
    {
        if(name.getText().matches("((ОАО)|(ЗАО)|(ООО)|(ИП)|(УП))\\s\"(([a-z]|[A-Z])|([а-я]|[А-Я])){3,}\""))
            return true;
        else
        {
            controller.showErrorNotification("Недопустимое значение поля.");
            controller.indicateError(name);
            return false;
        }
    }

    public boolean verifyAddress(TextInputControl address)
    {
        if(address.getText().matches("\\d{6}\\s.{5,}"))
            return true;
        else
        {
            controller.showErrorNotification("Недопустимое знаечние поля адрес.");
            controller.indicateError(address);
            return false;
        }
    }

    public boolean verifyPercents(TextInputControl percents)
    {
        if(percents.getText().matches("\\d{1,3}") && Integer.valueOf(percents.getText())<=100 && Integer.valueOf(percents.getText())>0)
            return true;
        else
        {
            controller.showErrorNotification("Неверное значение %.");
            controller.indicateError(percents);
            return false;
        }
    }

    public boolean verifyPasswordChange(TextInputControl currentPassword, TextInputControl newPassword, TextInputControl repeatNewPassword)
    {
        if(!newPassword.getText().isEmpty())
        {
            verifyPassword(newPassword);

            if(!currentPassword.getText().equals(Encryptor.decode(App.getCurrentUser().getPassword())))
            {
                controller.showErrorNotification("Неверный текущий пароль.");
                controller.indicateError(currentPassword);
            }

            verifyPasswordEquality(newPassword,repeatNewPassword);

            return !controller.isErrorState();
        }
        else
            return true;
    }
}
