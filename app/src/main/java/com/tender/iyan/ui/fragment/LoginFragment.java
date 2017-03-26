package com.tender.iyan.ui.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
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
import com.tender.iyan.ui.activity.HomeActivity;
import com.tender.iyan.util.UserUtil;

public class LoginFragment extends Fragment implements View.OnClickListener, UserService.LoginView {

    private EditText emailText;
    private EditText passwordText;
    private Button loginButton;
    private Button signUpButton;
    private ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emailText = (EditText) view.findViewById(R.id.et_email);
        passwordText = (EditText) view.findViewById(R.id.et_password);
        loginButton = (Button) view.findViewById(R.id.btn_login);
        if (loginButton != null)
            loginButton.setOnClickListener(this);

        signUpButton = (Button) view.findViewById(R.id.btn_sign_up);
        if (signUpButton != null)
            signUpButton.setOnClickListener(this);

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Tunggu sebentar...");
        dialog.setCancelable(false);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == loginButton.getId()) {
            if (emailText.getText().toString().equals("")) {
                emailText.setError("Email masih kosong");
                emailText.requestFocus();
            } else if (passwordText.getText().toString().equals("")) {
                passwordText.setError("Password masih kosong");
                passwordText.requestFocus();
            } else {
                User user = new User();
                user.setEmail(emailText.getText().toString());
                user.setPassword(passwordText.getText().toString());
                UserService.getInstance().login(this, user);
                dialog.show();
            }
        }

        if (view.getId() == signUpButton.getId()) {
            getFragmentManager().beginTransaction().replace(R.id.container_login, new SignUpFragment()).addToBackStack(null).commit();
        }
    }

    @Override
    public void onLoginSuccess(User user) {
        if (dialog.isShowing())
            dialog.dismiss();

        UserUtil.getInstance(getContext()).setLoginState(user.getId(), true);
        startActivity(new Intent(getContext(), HomeActivity.class));
        getActivity().finish();
        Toast.makeText(getContext(), "Login success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginFailed(String message) {
        if (dialog.isShowing())
            dialog.dismiss();

        Toast.makeText(getContext(), "Login failed, error : " + message, Toast.LENGTH_SHORT).show();
    }
}
