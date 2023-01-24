package com.example.bookworld.activities;

import static com.example.bookworld.prefrences.PreferencesManager.PREF_KEY_FIRST_NAME;
import static com.example.bookworld.prefrences.PreferencesManager.PREF_KEY_LAST_NAME;
import static com.example.bookworld.prefrences.PreferencesManager.PREF_KEY_LOGIN_STATUS;
import static com.example.bookworld.prefrences.PreferencesManager.PREF_KEY_USER_ID;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookworld.R;
import com.example.bookworld.beans.User;
import com.example.bookworld.database.async.user.GetAllUserAsyncTask;
import com.example.bookworld.database.async.user.InsertUserAsyncTask;
import com.example.bookworld.database.db.AppData;
import com.example.bookworld.database.db.DbResponse;
import com.example.bookworld.databinding.ActivityLoginBinding;
import com.example.bookworld.network.NetworkHelper;
import com.example.bookworld.prefrences.PreferencesManager;
import com.example.bookworld.utils.Result;
import com.example.bookworld.utils.ResultListener;
import com.example.bookworld.utils.Validator;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityLoginBinding binding;
    private ProgressDialog dialog;
    private NetworkHelper networkHelper;
    private PreferencesManager preferences;
    private Context context;
    ArrayList<User> allUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        networkHelper = NetworkHelper.getInstance(getApplicationContext());
        allUsers = new ArrayList<>();
        getAllUsers();
        initialize();
        hideActionbar();
        setListeners();
        autoLogin();
    }

    private void hideActionbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            return;
        }
        getSupportActionBar().hide();
    }

    private void initialize() {
        preferences = PreferencesManager.getInstance(this);
        context = LoginActivity.this;
    }

    private void setListeners() {
        binding.buttonSignIn.setOnClickListener(this);
        binding.textSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button_sign_in) {
            onLoginClicked();
        } else if (id == R.id.text_sign_up) {
            onSignUpClicked();
        }
    }

    private void onLoginClicked() {
        removeErrors();
        String email = binding.editTextUsername.getText().toString();
        String password = binding.editTextPassword.getText().toString();
        if (isInputValid(email, password)) {
            login(email, password);
        }
    }

    private void onSignUpClicked() {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    private void login(String username, String password) {
        showDialog();
        User user = new User(username, password);

        networkHelper.signInUser(user, new ResultListener<User>() {
            @Override
            public void onResult(Result<User> result) {
                Error error = (result != null) ? result.getError() : null;
                User resultUser = (result != null) ? result.getItem() : null;
                if ((result == null) || (error != null) || (resultUser == null)) {
                    String errorMsg = (error != null) ? error.getMessage() : getString(R.string.user_signIn_error);
                    Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    return;
                }
                user.setId(resultUser.getId());
                user.setUsername(resultUser.getUsername());
                user.setPassword(resultUser.getPassword());
                user.setFirstName(resultUser.getFirstName());
                user.setLastName(resultUser.getLastName());
                user.setEmail(resultUser.getEmail());
                user.setGender(resultUser.getGender());
                user.setSessionToken(resultUser.getSessionToken());
                user.setLoginStatus(true);

                InsertUserAsyncTask userInsertTask = new InsertUserAsyncTask(getApplicationContext(), new ResultListener<User>() {
                    @Override
                    public void onResult(Result<User> result) {
                        Error error = (result != null) ? result.getError() : null;
                        if ((result == null) || (error != null)) {
                            String errorMsg = (error != null) ? error.getMessage() : getString(R.string.user_signIn_error);
                            Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                            return;
                        }
                        AppData.setCurrentUser(user);
                        preferences.put(PREF_KEY_USER_ID, user.getId());
                        preferences.put(PREF_KEY_FIRST_NAME, user.getFirstName());
                        preferences.put(PREF_KEY_LAST_NAME, user.getLastName());
                        preferences.put(PREF_KEY_LOGIN_STATUS, true);
                        binding.editTextUsername.setText("");
                        binding.editTextPassword.setText("");
                        dialog.dismiss();
                        Intent intent = new Intent(LoginActivity.this, GenreActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                userInsertTask.execute(user);
            }
        });
    }

    private void showDialog() {
        String dialogTitle = getString(R.string.title_dialog);
        String dialogMessage = getString(R.string.title_dialog_message);
        dialog = ProgressDialog.show(this, dialogTitle, dialogMessage, true);
    }

    private void getAllUsers() {
        GetAllUserAsyncTask userGetAllTask = new GetAllUserAsyncTask(getApplicationContext(), new DbResponse<List<User>>() {
            @Override
            public void onSuccess(List<User> users) {
                allUsers.addAll(users);
            }

            @Override
            public void onError(Error error) {
                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        userGetAllTask.execute();
    }

    private void autoLogin() {
        boolean hasLoggedIn = preferences.get(PreferencesManager.PREF_KEY_LOGIN_STATUS, false);
        if (hasLoggedIn) {
            User currentUser = new User(preferences.get(PREF_KEY_USER_ID, ""), preferences.get(PREF_KEY_FIRST_NAME, ""), preferences.get(PREF_KEY_LAST_NAME, ""));
            AppData.setCurrentUser(currentUser);
            Intent intent = new Intent(LoginActivity.this, GenreActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private boolean isInputValid(String username, String password) {
        boolean result = true;

        if (!Validator.validate(username, Validator.PASSWORD)) {
            binding.editTextUsername.setError(getString(R.string.error_invalid_username));
            result = false;
        }

        if (!Validator.validate(password, Validator.PASSWORD)) {
            binding.editTextPassword.setError(getString(R.string.error_invalid_password));
            result = false;
        }
        return result;
    }

    private void removeErrors() {
        binding.editTextUsername.setError(null);
        binding.editTextPassword.setError(null);
    }
}