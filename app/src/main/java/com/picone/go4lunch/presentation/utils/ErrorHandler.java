package com.picone.go4lunch.presentation.utils;

import android.content.Context;
import android.widget.Toast;

public class ErrorHandler {
//TODO how to manage error



    //todo attribute string here, general error
    public enum ERROR_STATE{
        NO_ERROR,
        NO_CONNEXION_ERROR
    }

    //todo on view
    public void onConnexionError(Context context){
        Toast.makeText(context,"no connexion please reconnect your phone and try again",Toast.LENGTH_LONG).show();
    }

}
