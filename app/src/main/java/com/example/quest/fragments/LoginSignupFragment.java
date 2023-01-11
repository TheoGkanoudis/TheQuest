package com.example.quest.fragments;


import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.quest.R;
import com.example.quest.activities.MainActivity;
import com.example.quest.utilities.CustomEditText;
import com.example.quest.utilities.FragmentHelper;
import com.example.quest.utilities.PreferenceManager;
import com.example.quest.utilities.Variables;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LoginSignupFragment extends Fragment {
    public int state = Variables.SIGNUP;
    boolean emailValid = false;
    boolean usernameValid = false;
    boolean passwordValid = false;
    boolean repeatPasswordValid = false;
    public static Pattern username;
    public static Pattern password;


    CustomEditText emailTextEdit;
    CustomEditText usernameTextEdit;
    CustomEditText passwordTextEdit;
    CustomEditText repeatPasswordTextEdit;
    ShapeableImageView logo;
    TextInputLayout emailTextInput;
    TextInputLayout usernameTextInput;
    TextInputLayout passwordTextInput;
    TextInputLayout repeatPasswordTextInput;

    LinearLayout logInText;
    LinearLayout signUpText;
    MaterialTextView signUpMessage;
    MaterialTextView logInMessage;
    MaterialButton signUpTextButton;
    MaterialButton logInTextButton;
    MaterialButton signUpButton;
    MaterialButton logInButton;
    ProgressBar progressBar;

    TextView errorTextView;

    PreferenceManager preferenceManager;


    int login;
    int signup;

    //TODO THESE ARE TEMP VARIABLES UNTIL FINISHING DEVELOPMENT
    private final Boolean developing = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.login_signup, container, false);


        username = Pattern.compile(getString(R.string.usernamePattern));
        password = Pattern.compile(getString(R.string.passwordPattern));


        initViews(view);
        addListeners();
        clearTextFields();

        return view;

    }

    //adding listeners
    private void addListeners() {

        //sign up text button behaviour
        signUpTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reformLoginSignup();
                clearFocus(view);
            }
        });
        //log in text button behaviour
        logInTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (developing) {
                    MainMenuFragment mainMenuFragment = new MainMenuFragment();
                    FragmentHelper.changeToFragment(getParentFragmentManager(), mainMenuFragment, String.valueOf(R.string.main_menu_fragment_name));
                } else {
                    reformLoginSignup();
                    clearFocus(view);
                }
            }
        });
        //sign up button behaviour
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading(view, true);
                signup = Variables.SIGNUP_SUCCESSFUL;
                String usernameSt = String.valueOf(usernameTextEdit.getText()).trim();
                String emailSt = String.valueOf(emailTextEdit.getText()).trim();
                String passwordSt = String.valueOf(passwordTextEdit.getText());
                FirebaseFirestore database = FirebaseFirestore.getInstance();
                isAvailableEmail(usernameSt, emailSt, passwordSt, database);
            }
        });
        //log in button behaviour
        logInButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                loading(view, true);
                String emailOrUsername;
                String username = String.valueOf(usernameTextEdit.getText()).trim();
                String password = String.valueOf(passwordTextEdit.getText());
                FirebaseFirestore database = FirebaseFirestore.getInstance();
                if (isValidEmail(String.valueOf(usernameTextEdit.getText()).trim())) {
                    emailOrUsername = getString(R.string.db_em);
                } else emailOrUsername = getString(R.string.db_un);
                logIn(username, password, database, emailOrUsername);
            }
        });

        //email editText handling
        //while typing
        emailTextEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setErrorTextView(Variables.SIGNUP_SUCCESSFUL);
                emailValid = isValidEmail(String.valueOf(emailTextEdit.getText()).trim());
                checkEnableSignUpLogIn(signUpButton);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //when changing focus
        emailTextEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                setErrorTextView(Variables.SIGNUP_SUCCESSFUL);
                checkLogo();
                if (!b && !emailValid && !TextUtils.isEmpty(emailTextEdit.getText())) {
                    emailTextInput.setError(getString(R.string.error_email));
                } else {
                    emailTextInput.setError(null);
                }
                checkLogo();
            }
        });

        //username editText handling
        //while typing
        usernameTextEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String target = String.valueOf(charSequence).trim();
                setErrorTextView(Variables.SIGNUP_SUCCESSFUL);
                usernameValid = true;
                if (state == Variables.LOGIN) {
                    usernameValid = true;
                    checkEnableSignUpLogIn(logInButton);
                } else {
                    if (charSequence.length() == 0) {
                        usernameValid = true;
                        usernameTextInput.setError(null);
                    } else if (isValidUsername(target) == Variables.USER_LONG) {
                        usernameTextInput.setError(getString(R.string.username_long));
                        usernameValid = false;
                    } else if (isValidUsername(target) == Variables.USER_WRONG_CHARS) {
                        usernameTextInput.setError((getString(R.string.username_characters)));
                        usernameValid = false;
                    } else if (isValidUsername(target) == Variables.GOOD_USERNAME) {
                        usernameValid = true;
                        usernameTextInput.setError(null);
                    }
                    checkEnableSignUpLogIn(signUpButton);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        //when changing focus
        usernameTextEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                setErrorTextView(Variables.SIGNUP_SUCCESSFUL);
                checkLogo();
                if (!b && !TextUtils.isEmpty(usernameTextEdit.getText())) {
                    if (isValidUsername(usernameTextEdit.getText().toString().trim()) == Variables.USER_SHORT)
                        usernameTextInput.setError(getString(R.string.username_short));
                    else if (isValidUsername(usernameTextEdit.getText().toString().trim()) == Variables.GOOD_USERNAME) {
                        usernameTextInput.setError(null);
                    }
                }
                checkLogo();
            }
        });

        //password editText handling
        //while typing
        passwordTextEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setErrorTextView(Variables.SIGNUP_SUCCESSFUL);
                passwordValid = false;
                if (isValidPassword(charSequence) == Variables.GOOD_PASSWORD) {
                    passwordValid = true;
                }
                if (charSequence.length() > Variables.PASS_MAX_LENGTH) {
                    passwordTextInput.setError(getString(R.string.password_long));
                } else if (isValidPassword(charSequence) == Variables.PASS_WRONG_CHARS) {
                    passwordTextInput.setError((getString(R.string.password_characters)));
                } else if (isValidPassword(charSequence) == Variables.GOOD_PASSWORD || charSequence.length() == 0) {
                    passwordTextInput.setError(null);
                }
                if (state == Variables.SIGNUP) {
                    repeatPasswordValid = String.valueOf(repeatPasswordTextEdit.getText()).contentEquals(passwordTextEdit.getText()) && !TextUtils.isEmpty(passwordTextEdit.getText()) && !TextUtils.isEmpty(repeatPasswordTextEdit.getText());
                    checkEnableSignUpLogIn(signUpButton);
                } else {
                    checkEnableSignUpLogIn(logInButton);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        //when removing focus
        passwordTextEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                setErrorTextView(Variables.SIGNUP_SUCCESSFUL);
                checkLogo();
                if (state == Variables.SIGNUP) {
                    if (!b && !TextUtils.isEmpty(passwordTextEdit.getText())) {
                        passwordValid = false;
                        if (passwordTextEdit.length() == 0) {
                            passwordTextInput.setError(null);
                        }
                        if (isValidPassword(passwordTextEdit.getText()) == Variables.PASS_SHORT)
                            passwordTextInput.setError(getString(R.string.password_short));
                        else if (isValidPassword(passwordTextEdit.getText()) == Variables.GOOD_PASSWORD) {
                            passwordValid = true;
                            checkEnableSignUpLogIn(signUpButton);
                        }
                        if (state == Variables.SIGNUP) {
                            if (String.valueOf(passwordTextEdit.getText()).contentEquals(repeatPasswordTextEdit.getText()))
                                repeatPasswordValid = true;
                            else repeatPasswordValid = false;
                        }

                    }
                }
                //on log in screen
                else {
                    //TODO ONCE LOG-IN MECHANISM HAS BEEN ESTABLISHED: add username check
                }
                checkLogo();
            }
        });
        //after hitting check
        passwordTextEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == 66 && state == Variables.LOGIN) {
                    if (isValidPassword(passwordTextEdit.getText()) == Variables.GOOD_PASSWORD || passwordTextEdit.getText().length() == 0) {
                        passwordTextInput.setError(null);
                    }
                    checkEnableSignUpLogIn(signUpButton);
                    clearFocus(view);
                }
                return false;
            }
        });

        //repeat password editText handling
        //while typing
        repeatPasswordTextEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence) && !TextUtils.isEmpty((passwordTextEdit.getText()))) {
                    repeatPasswordValid = String.valueOf(charSequence).contentEquals(passwordTextEdit.getText());
                    checkEnableSignUpLogIn(signUpButton);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //when changing focus
        repeatPasswordTextEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                checkLogo();
                if (!b) {
                    if (!repeatPasswordValid && !TextUtils.isEmpty(passwordTextEdit.getText()) && !TextUtils.isEmpty(repeatPasswordTextEdit.getText())) {
                        repeatPasswordTextInput.setError(getString(R.string.pass_no_match));
                    } else {
                        repeatPasswordTextInput.setError(null);
                        checkEnableSignUpLogIn(signUpButton);
                    }

                } else {
                    repeatPasswordTextInput.setError(null);
                    checkLogo();
                    checkEnableSignUpLogIn(signUpButton);
                }
            }
        });
        //after hitting check
        repeatPasswordTextEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (!repeatPasswordValid && keyEvent.getKeyCode() == 66 && !TextUtils.isEmpty(passwordTextEdit.getText()) && !TextUtils.isEmpty(repeatPasswordTextEdit.getText())) {
                    repeatPasswordTextInput.setError(getString(R.string.pass_no_match));
                } else {
                    repeatPasswordTextInput.setError(null);
                    if (keyEvent.getKeyCode() == 66) {
                        checkEnableSignUpLogIn(signUpButton);
                        clearFocus(view);
                    }
                }
                return false;
            }
        });


    }

    //initiating views
    private void initViews(View view) {
        emailTextInput = view.findViewById(R.id.email_text_input);
        emailTextEdit = view.findViewById(R.id.email_text_edit);
        usernameTextInput = view.findViewById(R.id.username_text_input);
        usernameTextEdit = view.findViewById(R.id.username_text_edit);
        passwordTextInput = view.findViewById(R.id.password_text_input);
        passwordTextEdit = view.findViewById(R.id.password_text_edit);
        repeatPasswordTextInput = view.findViewById(R.id.repeat_password_text_input);
        repeatPasswordTextEdit = view.findViewById(R.id.repeat_password_text_edit);
        errorTextView = view.findViewById(R.id.error_tv);
        logo = view.findViewById(R.id.logo_img);
        logInText = view.findViewById(R.id.log_in_text);
        signUpText = view.findViewById(R.id.sign_up_text);
        signUpMessage = view.findViewById(R.id.logo_txt2_1);
        logInMessage = view.findViewById(R.id.logo_txt2_2);
        signUpTextButton = view.findViewById(R.id.sign_up_text_button);
        logInTextButton = view.findViewById(R.id.log_in_text_button);
        signUpButton = view.findViewById(R.id.sign_up_button);
        logInButton = view.findViewById(R.id.log_in_button);
        progressBar = view.findViewById(R.id.progress_bar);
    }

    //email validity check
    private static boolean isValidEmail(String target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    //username validity check
    private static int isValidUsername(String target) {
        Matcher matcher = username.matcher(target);
        if (!matcher.matches()) return Variables.USER_WRONG_CHARS;
        else if (target.length() < Variables.USER_MIN_LENGTH) return Variables.USER_SHORT;
        else if (target.length() > Variables.USER_MAX_LENGTH) return Variables.USER_LONG;
        else return Variables.GOOD_USERNAME;
    }

    //password validity check
    private static int isValidPassword(CharSequence target) {
        Matcher matcher = password.matcher(target);
        if (!matcher.matches()) return Variables.PASS_WRONG_CHARS;
        else if (target.length() < Variables.PASS_MIN_LENGTH) return Variables.PASS_SHORT;
        else if (target.length() > Variables.PASS_MAX_LENGTH) return Variables.PASS_LONG;
        else return Variables.GOOD_PASSWORD;
    }

    //sign up, log in button check
    private void checkEnableSignUpLogIn(MaterialButton button) {
        switch (state) {
            case Variables.SIGNUP:
                button.setEnabled(emailValid && usernameValid && passwordValid && repeatPasswordValid);
                break;
            case Variables.LOGIN:
                button.setEnabled(usernameValid && passwordValid);
                break;

        }
    }

    //logo visibility check
    private void checkLogo() {
        if (state == Variables.SIGNUP) {
            if (emailTextEdit.isFocused() || usernameTextEdit.isFocused() || passwordTextEdit.isFocused() || repeatPasswordTextEdit.isFocused()) {
                logo.setVisibility(View.GONE);
            } else logo.setVisibility(View.VISIBLE);
        }
    }

    //clear focus from all views
    private void clearFocus(View view) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        logo.setVisibility(View.VISIBLE);
        if (view instanceof MaterialButton) {
            emailTextEdit.clearFocus();
            usernameTextEdit.clearFocus();
            passwordTextEdit.clearFocus();
            repeatPasswordTextEdit.clearFocus();
        } else {
            view.clearFocus();
        }
    }

    //change the page between signup and login
    private void reformLoginSignup() {
        if (state == Variables.SIGNUP) {
            //change state
            state = Variables.LOGIN;
            //swap top text
            logInText.setVisibility(View.GONE);
            signUpText.setVisibility(View.VISIBLE);
            logInMessage.setVisibility(View.VISIBLE);
            signUpMessage.setVisibility(View.GONE);
            //adjust inputs
            emailTextInput.setVisibility(View.GONE);
            repeatPasswordTextInput.setVisibility(View.GONE);
            usernameTextInput.setHint(getString(R.string.hint_un_em));
            //swap buttons
            signUpButton.setVisibility(View.INVISIBLE);
            logInButton.setVisibility(View.VISIBLE);
            clearTextFields();
        } else {
            //change state
            state = Variables.SIGNUP;
            //swap top text
            logInText.setVisibility(View.VISIBLE);
            signUpText.setVisibility(View.GONE);
            logInMessage.setVisibility(View.GONE);
            signUpMessage.setVisibility(View.VISIBLE);
            //adjust inputs
            emailTextInput.setVisibility(View.VISIBLE);
            repeatPasswordTextInput.setVisibility(View.VISIBLE);
            usernameTextInput.setHint(getString(R.string.hint_username));
            //swap buttons
            signUpButton.setVisibility(View.VISIBLE);
            logInButton.setVisibility(View.INVISIBLE);
            //deactivate text fields
            usernameTextEdit.setText("");
            passwordTextEdit.setText("");
            //clear errors
            usernameTextInput.setError(null);
            passwordTextInput.setError(null);
            clearTextFields();
        }
    }

    //clear all textFields
    private void clearTextFields() {
        emailTextEdit.setText("");
        usernameTextEdit.setText("");
        passwordTextEdit.setText("");
        repeatPasswordTextEdit.setText("");

        emailTextInput.setError(null);
        usernameTextInput.setError(null);
        passwordTextInput.setError(null);
        repeatPasswordTextInput.setError(null);

        usernameValid = false;
        emailValid = false;
        passwordValid = false;
        repeatPasswordValid = false;
    }

    //change signed in status
    private void signedIn(boolean signedIn, String username) {
        PreferenceManager preferenceManager = MainActivity.preferenceManager;
        preferenceManager.putBoolean(Variables.KEY_IS_SIGNED_IN, signedIn);
        if (signedIn) {
            preferenceManager.putString(Variables.KEY_USERNAME, username);
        }
    }

    //loading bar
    private void loading(View button, Boolean isLoading) {
        if (isLoading) {
            button.setEnabled(false);
            button.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            button.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    //error TextView
    private void setErrorTextView(int error) {
        errorTextView.setVisibility(View.VISIBLE);
        switch (error) {
            case Variables.USERNAME_EXISTS:
                errorTextView.setText(getString(R.string.un_exists));
                break;
            case Variables.EMAIL_EXISTS:
                errorTextView.setText(getString(R.string.em_exists));
                break;
            case Variables.WRONG_USERNAME:
                errorTextView.setText(getString(R.string.wrong_un));
                break;
            case Variables.WRONG_PASSWORD:
                errorTextView.setText(getString(R.string.wrong_pw));
                break;
            case Variables.WRONG_USERNAME_PASSWORD:
                errorTextView.setText(getString(R.string.wrong_un_pw));
                break;
            default:
                errorTextView.setVisibility(View.INVISIBLE);
                break;
        }
    }

    //SIGNUP-1. sign up email validity check with firebase
    private void isAvailableEmail(String username, String email, String password, FirebaseFirestore database) {
        database.collection(getString(R.string.db_collection))
                .whereEqualTo(getString(R.string.db_em), email)
                .get()
                .addOnCompleteListener(emExists -> {
                    if (emExists.isSuccessful() && (emExists.getResult() == null || emExists.getResult().getDocuments().size() == 0)) {
                        isAvailableUsername(username, email, password, database);
                    } else {
                        signup = Variables.EMAIL_EXISTS;
                        signupProcedure(username, email, password, 0);
                    }
                });
    }

    //SIGNUP-2. sign up username availability check with firebase
    private void isAvailableUsername(String username, String email, String password, FirebaseFirestore database) {
        database.collection(getString(R.string.db_collection))
                .whereEqualTo(getString(R.string.db_un), username)
                .get()
                .addOnCompleteListener(unExists -> {
                    if (unExists.isSuccessful() && (unExists.getResult() == null || unExists.getResult().getDocuments().size() == 0)) {
                        setIdToGive(database, username, email, password);
                    } else {
                        signup = Variables.USERNAME_EXISTS;
                        signupProcedure(username, email, password, 0);
                    }
                });
    }

    //SIGNUP-3. get the max id to give new user next
    private void setIdToGive(FirebaseFirestore database, String username, String email, String password) {
        database.collection(getString(R.string.db_collection))
                .orderBy(getString(R.string.db_id), Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(sortedById -> {
                    if (sortedById.isSuccessful()) {
                        DocumentSnapshot lastId = sortedById.getResult().getDocuments().get(0);
                        int idToGive = (lastId.get(getString(R.string.db_id), int.class) + 1);
                        signupProcedure(username, email, password, idToGive);
                    }
                });
    }

    //SIGNUP-4: create user profile in database
    private void signupProcedure(String username, String email, String password, int id) {

        //add user to firebase instant
        if (signup == Variables.SIGNUP_SUCCESSFUL) {
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            HashMap<String, Object> data = new HashMap<>();
            data.put(getString(R.string.db_un), username);
            data.put(getString(R.string.db_em), email);
            data.put(getString(R.string.db_pw), password);
            data.put(getString(R.string.db_id), id);
            database.collection(getString(R.string.db_collection))
                    .add(data)
                    .addOnSuccessListener(documentReference -> {
                        signedIn(true, String.valueOf(username));
                        MainMenuFragment mainMenuFragment = new MainMenuFragment();
                        FragmentHelper.changeToFragment(getParentFragmentManager(), mainMenuFragment, String.valueOf(R.string.main_menu_fragment_name));
                        loading(signUpButton, false);
                    })
                    .addOnFailureListener(exception -> {
                        signup = Variables.SIGNUP_ERROR;
                        loading(signUpButton, false);
                    });
        } else {
            setErrorTextView(signup);
            loading(signUpButton, false);
        }
    }

    //LOGIN: check with firebase
    private void logIn(String username, String password, FirebaseFirestore database, String emailOrUsername) {

        PreferenceManager preferenceManager = MainActivity.preferenceManager;
        database.collection(getString(R.string.db_collection))
                .whereEqualTo(emailOrUsername, username)
                .whereEqualTo(getString(R.string.db_pw), password)
                .get()
                .addOnCompleteListener(unLogin -> {
                    if (unLogin.isSuccessful() && unLogin.getResult() != null
                            && unLogin.getResult().getDocuments().size() > 0) {
                        DocumentSnapshot documentSnapshot = unLogin.getResult().getDocuments().get(0);
                        preferenceManager.putString(Variables.KEY_EMAIL, documentSnapshot.getString(getString(R.string.db_em)));
                        preferenceManager.putInt(Variables.KEY_ID, documentSnapshot.get(getString(R.string.db_id), int.class));
                        Toast.makeText(getContext(), String.valueOf(preferenceManager.getInt(Variables.KEY_ID)), Toast.LENGTH_SHORT).show();
                        signedIn(true, documentSnapshot.getString(getString(R.string.db_un)));
                        MainMenuFragment mainMenuFragment = new MainMenuFragment();
                        FragmentHelper.changeToFragment(getParentFragmentManager(), mainMenuFragment, String.valueOf(R.string.main_menu_fragment_name));
                        loading(logInButton, false);
                    } else {
                        //todo separate wrong username from wrong password messages
                        login = Variables.WRONG_USERNAME_PASSWORD;
                        setErrorTextView(login);
                        loading(logInButton, false);
                    }
                });
    }

}
