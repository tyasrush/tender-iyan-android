package com.tender.iyan.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tender.iyan.R;
import com.tender.iyan.entity.User;
import com.tender.iyan.service.UserService;

public class SignUpFragment extends Fragment implements UserService.SignUpView, View.OnClickListener {

    private EditText emailText;
    private EditText passwordText;
    private EditText nameText;
    private EditText contactText;
    private EditText addressText;
    private Button signUpButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emailText = (EditText) view.findViewById(R.id.et_email);
        passwordText = (EditText) view.findViewById(R.id.et_password);
        nameText = (EditText) view.findViewById(R.id.et_name);
        contactText = (EditText) view.findViewById(R.id.et_contact);
        addressText = (EditText) view.findViewById(R.id.et_address);
        signUpButton = (Button) view.findViewById(R.id.btn_sign_up);
        if (signUpButton != null)
            signUpButton.setOnClickListener(this);
    }

    @Override
    public void onSignUpSuccess() {
//        getFragmentManager().beginTransaction().replace(R.id.container_login, new LoginFragment()).commit();
        getFragmentManager().popBackStack();
        Toast.makeText(getContext(), "Sign Up success, please login with your account", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSignUpFailed(String message) {
        Toast.makeText(getContext(), "Sign Up failed, error : " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == signUpButton.getId()) {
            if (emailText.getText().toString().equals("")) {
                emailText.setError("Email masih kosong");
                emailText.requestFocus();
            } else if (passwordText.getText().toString().equals("")) {
                passwordText.setError("Password masih kosong");
                passwordText.requestFocus();
            }else if (nameText.getText().toString().equals("")) {
                nameText.setError("Nama masih kosong");
                nameText.requestFocus();
            }else if (contactText.getText().toString().equals("")) {
                contactText.setError("Kontak masih kosong");
                contactText.requestFocus();
            }else if (addressText.getText().toString().equals("")) {
                addressText.setError("Alamat masih kosong");
                addressText.requestFocus();
            } else {
                User user = new User();
                user.setEmail(emailText.getText().toString());
                user.setPassword(passwordText.getText().toString());
                user.setName(nameText.getText().toString());
                user.setContact(contactText.getText().toString());
                user.setAlamat(addressText.getText().toString());
                UserService.getInstance().signUp(this, user);
            }
        }
    }
}
