package com.arwallpaper;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.ar.core.Anchor;
import com.google.ar.core.Config;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Camera;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArFragment arFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);

        setOnTapActions();
        setSessionConfigs();

        ImageView iconMenu = findViewById(R.id.icon_menu);
        FloatingActionButton fabSettings = findViewById(R.id.fab_settings);


        iconMenu.setOnClickListener((view) -> {
            PictureListDialogFragment.newInstance().show(getSupportFragmentManager(), "dialog");
        });
        fabSettings.setOnClickListener((view) -> {
            removeAllModelsFromScene();
        });
    }


    private void setOnTapActions() {
        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {

            Anchor anchor = hitResult.createAnchor();
            ViewRenderable.builder()
                    .setView(this, R.layout.view_model_wallpaper)
                    .build()
                    .thenAccept(viewRenderable -> addModelToScene(anchor, viewRenderable))
                    .exceptionally(throwable -> {
                        Toast.makeText(this, "Unable to load renderable view", Toast.LENGTH_LONG).show();
                        return null;
                    });

        });
    }

    private void setSessionConfigs() {
        arFragment.setOnSessionConfigurationListener((session, config) -> {
            boolean isDepthSupported = session.isDepthModeSupported(Config.DepthMode.AUTOMATIC);
            if (isDepthSupported) {
                config.setDepthMode(Config.DepthMode.AUTOMATIC);
            }
            config.setPlaneFindingMode(Config.PlaneFindingMode.VERTICAL);
            config.setLightEstimationMode(Config.LightEstimationMode.AMBIENT_INTENSITY);
            config.setInstantPlacementMode(Config.InstantPlacementMode.LOCAL_Y_UP);
            session.configure(config);
        });
    }

    private void addModelToScene(Anchor anchor, ViewRenderable viewRenderable) {
        viewRenderable.setShadowCaster(false);
        viewRenderable.setVerticalAlignment(ViewRenderable.VerticalAlignment.CENTER);
        AnchorNode anchorNode = new AnchorNode(anchor);
        TransformableNode transformableNode = new TransformableNode(arFragment.getTransformationSystem());
        transformableNode.setParent(anchorNode);

        transformableNode.setRenderable(viewRenderable);

        arFragment.getArSceneView().getScene().addChild(anchorNode);
        transformableNode.select();
        transformableNode.setLookDirection(new Vector3(0, 0, 1));


        ImageButton btnRight = viewRenderable.getView().findViewById(R.id.btn_arrow_right);
        ImageButton btnLeft = viewRenderable.getView().findViewById(R.id.btn_arrow_left);
        ImageView imgWallpaper = viewRenderable.getView().findViewById(R.id.img_wallpaper);
        updateWallpaper(imgWallpaper);

        btnRight.setOnClickListener((view)->{
            Util.animateScaleUp(btnRight);
            WallpaperStore.updateIndex(1);
            updateWallpaper(imgWallpaper);
        });

        btnLeft.setOnClickListener((view)->{
            Util.animateScaleUp(btnLeft);
            WallpaperStore.updateIndex(-1);
            updateWallpaper(imgWallpaper);
        });
    }

    private void updateWallpaper(ImageView imgWallpaper) {
        Glide.with(this).load("file:///android_asset/" + WallpaperStore.wallpapers[WallpaperStore.selectedIndex]).into(imgWallpaper);
    }


    // Utils ....
    private void removeAllModelsFromScene() {
        List<Node> children = new ArrayList<>(arFragment.getArSceneView().getScene().getChildren());
        for (Node node : children) {
            if (node instanceof AnchorNode) {
                if (((AnchorNode) node).getAnchor() != null) {
                    ((AnchorNode) node).getAnchor().detach();
                }
            }
            if (!(node instanceof Camera)) {
                node.setParent(null);
            }
        }
    }
}