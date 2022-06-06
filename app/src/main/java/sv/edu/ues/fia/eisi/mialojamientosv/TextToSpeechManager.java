package sv.edu.ues.fia.eisi.mialojamientosv;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.content.Context;
import android.os.Bundle;
import java.util.Locale;

public class TextToSpeechManager {
    private TextToSpeech mTts=null;
    private boolean isLoaded=false;

    public void init(Context context){
        try{
            mTts=new TextToSpeech(context,onInitListener);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private TextToSpeech.OnInitListener onInitListener = new TextToSpeech.OnInitListener(){
        @Override
        public void onInit(int status) {
            Locale spanish = new Locale("es", "ES");
            if(status == TextToSpeech.SUCCESS){
                //Seleccionemos el lenguaje
                int result = mTts.setLanguage(spanish);
                isLoaded=true;
                if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("Error", "Este lenguaje no está permitido");
                }
            } else{
                Log.e("Error", "Fallo de inicio");

            }
        }
    };

    public void apagar(){
        mTts.shutdown();
    }

    public void agregarCola(String text){
        if(isLoaded){
            int i=mTts.speak(text, TextToSpeech.QUEUE_ADD,null);
        }else{
            Log.e("Error","TTS no inicializado");
        }
    }

    public void iniciarCola(String text){
        if(isLoaded){
            int i=mTts.speak(text, TextToSpeech.QUEUE_FLUSH,null);
        }else{
            Log.e("Error","TTS no inicializado");
        }
    }
}
