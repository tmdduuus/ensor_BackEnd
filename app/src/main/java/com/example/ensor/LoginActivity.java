
package com.example.ensor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

    public class LoginActivity extends AppCompatActivity {
        private static final String TAG = "LoginActivity";
        private View loginButton, logoutButton;
        private TextView nickName;
        private TextView email;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            loginButton = findViewById(R.id.login);
            logoutButton = findViewById(R.id.logout);
            nickName = findViewById(R.id.nickname);
            email = findViewById(R.id.email);
            
            Function2<OAuthToken, Throwable, Unit> callback = new  Function2<OAuthToken, Throwable, Unit>() {
                @Override
                public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                    // 이때 토큰이 전달이 되면 로그인이 성공한 것이고 토큰이 전달되지 않았다면 로그인 실패
                    if(oAuthToken != null) {

                    }
                    if (throwable != null) {

                    }
                    updateKakaoLoginUi();
                    return null;
                }
            };
            // 로그인 버튼
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(UserApiClient.getInstance().isKakaoTalkLoginAvailable(LoginActivity.this)) {
                        UserApiClient.getInstance().loginWithKakaoTalk(LoginActivity.this, callback);
                    }else {
                        UserApiClient.getInstance().loginWithKakaoAccount(LoginActivity.this, callback);
                    }
                }
            });
            // 로그 아웃 버튼
            logoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UserApiClient.getInstance().logout(new Function1<Throwable, Unit>() {
                        @Override
                        public Unit invoke(Throwable throwable) {
                            updateKakaoLoginUi();
                            return null;
                        }
                    });
                }
            });
            updateKakaoLoginUi();
        }
        private  void updateKakaoLoginUi(){
            UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
                @Override
                public Unit invoke(User user, Throwable throwable) {
                    // 로그인이 되어있으면
                    if (user!=null){

                        // 유저의 아이디
                        Log.d(TAG,"invoke: id" + user.getId());
                        // 유저의 어카운트정보에 이메일
                        Log.d(TAG,"invoke: nickname" + user.getKakaoAccount().getEmail());
                        // 유저의 어카운트 정보의 프로파일에 닉네임
                        Log.d(TAG,"invoke: email" + user.getKakaoAccount().getProfile().getNickname());

                        nickName.setText(user.getKakaoAccount().getProfile().getNickname());
                        email.setText(user.getKakaoAccount().getEmail());
                        loginButton.setVisibility(View.GONE);
                        logoutButton.setVisibility(View.VISIBLE);
                    }else {
                        // 로그인이 되어 있지 않다면 위와 반대로
                        nickName.setText(null);
                        email.setText(null);
                        loginButton.setVisibility(View.VISIBLE);
                        logoutButton.setVisibility(View.GONE);
                    }
                    return null;
                }
            });
        }
    };

