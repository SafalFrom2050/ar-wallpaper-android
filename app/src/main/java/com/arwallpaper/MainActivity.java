package com.arwallpaper;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.ar.core.Anchor;
import com.google.ar.core.Config;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

public class MainActivity extends AppCompatActivity {

    ArFragment arFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);

        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {

            if (arFragment.getArSceneView().getArFrame().getUpdatedAnchors().size() > 1) {
                return;
            }

            Anchor anchor = hitResult.createAnchor();
            ViewRenderable.builder()
                    .setView(this, R.layout.view_model_title)
                    .build()
                    .thenAccept(viewRenderable -> addModelToScene(anchor, viewRenderable))
                    .exceptionally(throwable -> {
                        Toast.makeText(this, "Unable to load renderable view", Toast.LENGTH_LONG).show();
                        return null;
                    });
        });
        arFragment.setOnSessionConfigurationListener((session, config) -> {
            boolean isDepthSupported = session.isDepthModeSupported(Config.DepthMode.AUTOMATIC);
            if (isDepthSupported) {
                config.setDepthMode(Config.DepthMode.AUTOMATIC);
            }
            config.setPlaneFindingMode(Config.PlaneFindingMode.VERTICAL);
            session.configure(config);
        });
    }


    void addModelToScene(Anchor anchor, ViewRenderable viewRenderable) {
        AnchorNode anchorNode = new AnchorNode(anchor);
        TransformableNode transformableNode = new TransformableNode(arFragment.getTransformationSystem());
        transformableNode.setParent(anchorNode);

        transformableNode.setRenderable(viewRenderable);

        arFragment.getArSceneView().getScene().addChild(anchorNode);
        transformableNode.select();
        transformableNode.setLookDirection(new Vector3(0, 0, 1));
    }
}