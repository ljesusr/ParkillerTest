package tools;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;


public class AndroidFuntions {

    Activity activity;
    public AndroidFuntions(Activity activity){
        this.activity=activity;
    }

    public void sendApptoBackground(){
        Intent i = new Intent();
        i.setAction(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        activity.startActivity(i);
        showMessageInToast("La aplicacion seguira en background");
    }

    public void exitApp(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Estas seguro que quieres cerrar la aplicación")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                       activity.finish();
                    }

                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void showMessageInToast(String message){
        Toast.makeText(activity,message, Toast.LENGTH_LONG).show();
    }

}
