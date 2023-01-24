package com.example.bookworld.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookworld.R;
import com.example.bookworld.beans.User;
import com.example.bookworld.databinding.ActivitySignUpBinding;
import com.example.bookworld.exceptions.InvalidUserException;
import com.example.bookworld.network.NetworkHelper;
import com.example.bookworld.utils.Result;
import com.example.bookworld.utils.ResultListener;
import com.example.bookworld.utils.Validator;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivitySignUpBinding binding;
    private NetworkHelper networkHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        networkHelper = NetworkHelper.getInstance(getApplicationContext());
        setUpActionBar();
        setListeners();
    }

    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            return;
        }
        actionBar.show();
    }

    private void setListeners() {
        binding.buttonConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button_confirm) {
            signUpUser();
        }
    }

    private void signUpUser() {
        removeErrors();
        try {
            User user = readUserInformation();
            save(user);
        } catch (InvalidUserException e) {
            e.printStackTrace();
        }
    }

    private void save(User user) {
        networkHelper.signupUser(user, new ResultListener<User>() {
            @Override
            public void onResult(Result<User> result) {
                Error error = (result != null) ? result.getError() : null;
                if ((result == null) || (error != null)) {
                    String errorMsg = (error != null) ? error.getMessage() : getString(R.string.user_signup_error);
                    Toast.makeText(SignUpActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(SignUpActivity.this, R.string.user_signup_success, Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void removeErrors() {
        binding.editTextConfirmPassword.setError(null);
        binding.editTextSignUpPassword.setError(null);
        binding.editTextEmail.setError(null);
        binding.editTextFirstName.setError(null);
        binding.editTextLastName.setError(null);
    }

    private User readUserInformation() throws InvalidUserException {
        String username = binding.editTextUsername.getText().toString();
        String password = binding.editTextSignUpPassword.getText().toString();
        String firstName = binding.editTextFirstName.getText().toString();
        String lastName = binding.editTextLastName.getText().toString();
        String email = binding.editTextEmail.getText().toString();
        String confirmPassword = binding.editTextConfirmPassword.getText().toString();
        String gender = getGender();

        User user = new User(username, password, firstName, lastName, email, gender, false);

        boolean isValidInformation = validateInputInformation(user, confirmPassword);
        if (isValidInformation) {
            return user;
        }
        throw new InvalidUserException();
    }

    private String getGender() {
        if (binding.radioButtonMale.isChecked()) {
            return binding.radioButtonMale.getText().toString();
        } else if (binding.radioButtonFemale.isChecked()) {
            return binding.radioButtonFemale.getText().toString();
        }
        return null;
    }

    private boolean validateInputInformation(User user, String confirmPassword) {
        boolean result = true;

        if (!user.getPassword().equals(confirmPassword)) {
            binding.editTextConfirmPassword.setError(getString(R.string.error_unique_password));
            result = false;
        }

        if (!Validator.validate(user.getPassword(), Validator.PASSWORD)) {
            binding.editTextSignUpPassword.setError(getString(R.string.error_invalid_password));
            result = false;
        }

        if (!Validator.validate(user.getPassword(), Validator.TEXT)) {
            binding.editTextSignUpPassword.setError(getString(R.string.error_invalid_password));
            result = false;
        }

        if (!Validator.validate(confirmPassword, Validator.TEXT)) {
            binding.editTextConfirmPassword.setError(getString(R.string.error_invalid_password));
            result = false;
        }

        if (!Validator.validate(user.getEmail(), Validator.EMAIL)) {
            binding.editTextEmail.setError(getString(R.string.error_invalid_email));
            result = false;
        }

        if (!Validator.validate(user.getFirstName(), Validator.TEXT)) {
            binding.editTextFirstName.setError(getString(R.string.error_invalid_first_name));
            result = false;
        }

        if (!Validator.validate(user.getLastName(), Validator.TEXT)) {
            binding.editTextLastName.setError(getString(R.string.error_invalid_last_name));
            result = false;
        }
        return result;
    }
}