package com.pagatodo.apolo.activity.login._presenter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.pagatodo.apolo.App;
import com.pagatodo.apolo.R;
import com.pagatodo.apolo.activity.ResetContraseniaUser;
import com.pagatodo.apolo.activity.login._presenter._interfaces.LoginInteractor;
import com.pagatodo.apolo.data.local.Preferences;
import com.pagatodo.apolo.data.model.webservice.request.IniciativasRequest;
import com.pagatodo.apolo.data.model.webservice.request.ValidaUserRequest;
import com.pagatodo.apolo.data.model.webservice.response.GetPromotersResponse;
import com.pagatodo.apolo.data.model.webservice.response.ValidateUserResponse;
import com.pagatodo.apolo.data.remote.BuildRequest;
import com.pagatodo.apolo.data.room.DatabaseManager;
import com.pagatodo.apolo.data.room.entities.Promotor;
import com.pagatodo.networkframework.DataManager;
import com.pagatodo.networkframework.interfaces.IRequestResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.pagatodo.apolo.data.remote.RequestContract.GET_PROMOTERS;
import static com.pagatodo.apolo.data.remote.RequestContract.POST_VALIDAUSER;
import static com.pagatodo.networkframework.UtilsNet.isOnline;


/**
 * Created by rvargas on 21-07-17.
 */

public class LoginInteractorImpl implements LoginInteractor ,IRequestResult {
    Preferences prefs = App.getInstance().getPrefs();
    Context context = App.getInstance();

    private Promotor validateUser(String passPromotor) {
        List<Promotor> promotores = new ArrayList<>();
        try {
            promotores = new DatabaseManager().getPromotorList();
        } catch (ExecutionException|InterruptedException e) {
            e.printStackTrace();
        }
        for (Promotor promotor : promotores) {
            if (promotor.getPromotor().equalsIgnoreCase(passPromotor)) {
                return promotor;
            }
        }
        return null;
    }

    @Override
    public void onLogin(String username, onLoginListener listener) {
        Promotor promotor = validateUser(username);
        if (TextUtils.isEmpty(username)) {
            listener.onUserNumberError();
            listener.onUserPassError();
        } else if (promotor != null) {
            listener.onSuccess(promotor);
        } else {
            listener.failure(context.getString(R.string.error_invalid_id));
        }
    }

    @Override
    public void onLoginNewI(String username, String pass, String imei, onLoginListener listener) {
        if (isOnline(this.context)) {


            BuildRequest.validateUserRequest(this, new ValidaUserRequest(username,pass,imei));
        }


    }

    @Override
    public void onSuccess(DataManager dataManager) {
        if (dataManager.getData() != null) {
            switch (dataManager.getMethod()) {
                case POST_VALIDAUSER:
                    prosesgetValidateUserResponse( (ValidateUserResponse) dataManager.getData());
                    break;
            }
        }

    }

    private void prosesgetValidateUserResponse(ValidateUserResponse data) {
        ValidateUserResponse element = (ValidateUserResponse)data;

        if (element.getRespuesta().getCodigo()!=0){

        }else {

            if (element.getPromotor().isResetContraseña()) {



                /***
                 *
                 * aca haces un fragment paa que cambie la contraseña
                 */
            }else {
                /***
                 * Go to main menu with session
                  */
            }

        }


    }

    @Override
    public void onFailed(DataManager dataManager) {

    }
}
