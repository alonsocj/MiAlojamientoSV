package sv.edu.ues.fia.eisi.mialojamientosv.ui.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;

import sv.edu.ues.fia.eisi.mialojamientosv.R;


public class FavoritosFragment extends Fragment {

    LottieAnimationView botonf;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favoritos, container, false);
        botonf = (LottieAnimationView) view.findViewById(R.id.animationFavorite);
        botonf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                botonf.playAnimation();
                //botonf.setProgress(0);
            }
        });
        return view;
    }
}