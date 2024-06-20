
import java.io.IOException;
import java.time.LocalDate;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Separator;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainSceneController {

    @FXML
    private DatePicker DOB;

    @FXML
    private PasswordField Npass;

    @FXML
    private TextField SuserName;

    @FXML
    private PasswordField confPass;

    @FXML
    private TextField email;

    @FXML
    private TextField fName;

    @FXML
    private TextField lName;

    @FXML
    private AnchorPane loginAnchor;

    @FXML
    private TextField password;

    @FXML
    private Label showLoginMessage;

    @FXML
    private Label showPassMessage;

    @FXML
    private Label showUsername;

    @FXML
    private Label showUsernameMessage;

    @FXML
    private AnchorPane signUpAnchor;

    @FXML
    private DialogPane dialogBox;

    @FXML
    private TextField forgotEmail;

    @FXML
    private Label showConfPassMessage;

    @FXML
    private Label showEmailMessage;

    @FXML
    private Label emailCode;

    @FXML
    private AnchorPane forgotInformation;

    @FXML
    private AnchorPane forgotAnchor;

    @FXML
    private Label sentAgain;

    @FXML
    private TextField verificationCode;

    @FXML
    private Label showPassowordMessage;

    @FXML
    private TextField username;

    @FXML
    private Label emailNotPresent;

    @FXML
    private AnchorPane enterCodeAnchor;

    @FXML
    private AnchorPane welcomePge;

    @FXML
    private Label codeInformation;

    @FXML
    private Label newConfPassInfo;

    @FXML
    private PasswordField newConfirmPassF;

    @FXML
    private Label newPassInfo;

    @FXML
    private Separator newPassDialogBoxSep;

    @FXML
    private DialogPane newPasswordDialogBox;

    @FXML
    private PasswordField newPasswordF;

    @FXML
    private AnchorPane resetPassword;

    @FXML
    private AnchorPane signUpAnchor2;

    @FXML
    private Button onClickBtn;

    @FXML
    private Button onClickBtn2;

    @FXML
    private Button onClickContinue;

    @FXML
    private Hyperlink onClickOK;

    @FXML
    private Button onCliclkSend;

    int code;

    @FXML

    // checking the verification code is valid or not
    void onClickContinue(ActionEvent event) {
        onClickContinue.setUnderline(true);
        String vCode = verificationCode.getText();
        int veriCode = Integer.parseInt(vCode);

        if (veriCode == code) {
            resetPassword.setVisible(true);
        } else {
            codeInformation.setVisible(true);
        }
    }
    String aEmail;
    String email2;

    @FXML

    // resend the verification code to the email
    void onClickRecieveCode(ActionEvent event) {
        if (aEmail.length()==0){
            makeConnection.SendingEmail(email2, code);
        }
        else{
            makeConnection.SendingEmail(aEmail, code);
        }
    }

    @FXML

    // getting your new account details and stored in database
    void onClickBtn2(ActionEvent event) {
        String Username = SuserName.getText();
        int uniqueUsername;
        int checkUsernameUpperCase;
        int checkUsernameHaveDigit;

        // check whether the username is valid or not
        uniqueUsername = makeConnection.userNameValidation(Username);

        // check in username is have any upperCase letter or not
        checkUsernameUpperCase = makeConnection.haveUpperCase(Username);

        // check in username is have any digit letter or not
        checkUsernameHaveDigit = makeConnection.isHaveAnyDigit(Username);

        // check whether the username is already in database or not 
        if (uniqueUsername == 0) {
            showUsernameMessage.setText("This username is already taken!!");
            showUsernameMessage.setVisible(true);
        } else if (checkUsernameUpperCase == 2) {
            showUsernameMessage.setText("Username must be all letters in small case!!");
            showUsernameMessage.setVisible(true);
        } else if (checkUsernameHaveDigit == 1) {
            showUsernameMessage.setText("Username must be at least have one number!!");
            showUsernameMessage.setVisible(true);
        } else {
            showUsernameMessage.setVisible(false);
            String firstName = fName.getText();
            String lastName = lName.getText();
            String Name = firstName + " " + lastName;
            String Email = email.getText();

            int checkValidEmail;
            checkValidEmail = makeConnection.emailValidation(Email);
            int checkEmail = makeConnection.checkValidEmail(Email);
            System.out.println(checkValidEmail);

            if (checkValidEmail == 0) {
                showEmailMessage.setVisible(true);
            } else if (checkEmail == 1) {
                showEmailMessage.setText("Please enter a valid email");
                showEmailMessage.setVisible(true);
            } else {
                showEmailMessage.setVisible(false);
                String newPassword = Npass.getText();
                int checkPass;

                // check your password is strong or weak
                checkPass = makeConnection.checkPassword(newPassword);

                if (checkPass == 1) {
                    showPassowordMessage.setVisible(true);
                } else {
                    showPassowordMessage.setVisible(false);
                    String confirmPass = confPass.getText();

                    // check your confirm password is match with your new password or not
                    int checkConfPass = makeConnection.checkConfirmPassword(confirmPass, newPassword);

                    if (checkConfPass == 0) {
                        showConfPassMessage.setVisible(true);
                    } else {
                        showConfPassMessage.setVisible(false);
                        LocalDate Date_of_birth = DOB.getValue();
                        String dob = Date_of_birth.toString();

                        // complete the signup process
                        makeConnection.signUp(Name, Email, Username, dob, newPassword, confirmPass);

                        // when the account is created successfully
                        dialogBox.setVisible(true);
                    }
                }
            }
        }
    }

    @FXML

    // open the forgot password page
    void onClickForgot(ActionEvent event) {
        forgotAnchor.setVisible(true);
    }

    @FXML

    // open the signup page
    void onClickNewAccount(ActionEvent event) {
        Parent homePage;
        Scene homePageScene;
        Stage appStage;
        try {
            homePage = FXMLLoader.load(getClass().getResource("MainScene2.fxml"));
            homePageScene = new Scene(homePage);
            appStage = (Stage) ((Node)event.getSource()).getScene().getWindow();

            appStage.setScene(homePageScene);
            appStage.show();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @FXML

    // logged you in your account
    void onClickBtn(ActionEvent event) {
        onClickBtn.setUnderline(true);
        String usName = username.getText();
        String lPass = password.getText();
        int result = makeConnection.userLoginAuthentication(usName, lPass);
        System.out.println(result);

        if (result != 0) {
            showLoginMessage.setVisible(true);
        } else {
            showUsername.setText(usName);
            welcomePge.setVisible(true);
        }

    }

    @FXML

    // open the signUp page
    void onClickSignUp(ActionEvent event) {
        // signUpAnchor.setVisible(true);
        Parent homePage;
        Scene homePageScene;
        Stage appStage;
        try {
            homePage = FXMLLoader.load(getClass().getResource("MainScene2.fxml"));
            homePageScene = new Scene(homePage);
            appStage = (Stage) ((Node)event.getSource()).getScene().getWindow();

            appStage.setScene(homePageScene);
            appStage.show();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    @FXML

    // send the verification code to entered email
    void onCliclkSend(ActionEvent event) {
        onCliclkSend.setUnderline(true);
        email2 = forgotEmail.getText();

        // check that you entered is email or username
        int checkforgotemail = makeConnection.checkForgotEmail(email2);

        if (checkforgotemail == 1) {

            // getting random numbers of verification Code
            code = makeConnection.getRandomNumbers();

            // check username valid or not
            int checkValidUserName = makeConnection.userNameValidation(email2);


            if (checkValidUserName == 1) {
                emailNotPresent.setVisible(true);
            } else {
                emailNotPresent.setVisible(false);
                aEmail = makeConnection.getEmail(email2);
                emailCode.setText(aEmail);
                forgotInformation.setVisible(true);
                makeConnection.SendingEmail(aEmail, code);
            }
        } else {

            // check the email is already present or not
            int checkEmailIsPresent = makeConnection.emailValidation(email2);

            if (checkEmailIsPresent == 1) {
                emailNotPresent.setVisible(true);
            } else {
                emailNotPresent.setVisible(false);

                // generating random numbers of verification Code
                code = makeConnection.getRandomNumbers();
                emailCode.setText(email2);
                forgotInformation.setVisible(true);

                // sending code via email
                makeConnection.SendingEmail(email2, code);
            }
        }
    }

    @FXML

    // open the login page
    void onClickAnotherAcc(ActionEvent event) {
        loginAnchor.setVisible(true);
    }

    @FXML

    // open the enter verification code page
    void onClickOK(ActionEvent event) {
        onClickOK.setUnderline(true);
        enterCodeAnchor.setVisible(true);
    }

    @FXML

    // set your new password
    void onClickOtherContinue(ActionEvent event) {
        String newPass = newPasswordF.getText();

        int checkPass;
        checkPass = makeConnection.checkPassword(newPass);

        if (checkPass == 1) {
            newPassInfo.setVisible(true);
        } else {
            newPassInfo.setVisible(false);
            String newConfPass = newConfirmPassF.getText();

            int checkConfPass = makeConnection.checkConfirmPassword(newConfPass, newPass);

            if (checkConfPass == 0) {
                newConfPassInfo.setVisible(true);
            } else {
                newConfPassInfo.setVisible(false);
                makeConnection.changePassword(email2, newPass, newConfPass);
                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                    System.out.println(e);
                }
                newPasswordDialogBox.setVisible(true);
                newPassDialogBoxSep.setVisible(true);
            }
        }

    }

}
