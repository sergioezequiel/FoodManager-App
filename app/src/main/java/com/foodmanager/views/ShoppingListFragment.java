package com.foodmanager.views;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.foodmanager.R;
import com.foodmanager.adapters.RecipeAdapter;
import com.foodmanager.adapters.ShoppingAdapter;
import com.foodmanager.listeners.ShoppingListListener;
import com.foodmanager.models.ItemDespensa;
import com.foodmanager.models.RecipeItem;
import com.foodmanager.models.ShoppingItem;
import com.foodmanager.models.SingletonDatabaseManager;
import com.foodmanager.models.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.security.Signature;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class ShoppingListFragment extends Fragment implements ShoppingListListener {
    private RecyclerView inventoryRecyclerView;
    private ShoppingAdapter inventoryAdapter;
    private RecyclerView.LayoutManager inventoryLayoutManager;
    private ArrayList<ShoppingItem> inventoryItems = new ArrayList<>();
    private ItemTouchHelper.SimpleCallback inventoryCallBack;
    private TextInputLayout textInputLayout;
    private AutoCompleteTextView dropDownText;
    private FloatingActionButton fab_add_item_shopping;
    private EditText addItem;
    private Button addImagem;
    private ImageView imageView;
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    private Bitmap imgbt;
    private Button tirarFoto;
    private static final int REQUEST_CODE_CAMERA = 11;

    private SwipeRefreshLayout swipeRefreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View root = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        return root;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState ) {

        super.onViewCreated(view, savedInstanceState);

        inventoryRecyclerView = view.findViewById(R.id.shoppingRecyclerView);

        prepareRecyclerView();

        swipeRefreshLayout = view.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.nivel6));
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
        fab_add_item_shopping = view.findViewById(R.id.fab_add_item_shopping);
        fab_add_item_shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editInventoryDialog();
            }
        });

        SingletonDatabaseManager.getInstance(getContext()).setShoppingListListener(this);
        inventoryAdapter.setOnItemClickListener(new ShoppingAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                SingletonDatabaseManager.getInstance(getContext()).deleteItemShopping(inventoryAdapter.InventoryList.get(position).getIditem());
            }
        });

    }

    private void selecionarImagem() {

        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , 1);


    }
    private void tirarFoto() {

        Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, 0);

    }



    //Funcao para criar um menu de search
    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = new SearchView(((MainActivity) getContext()).getSupportActionBar().getThemedContext());


        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setActionView(searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                inventoryAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    //Funcao para perparar o recycler view e por os itens dentro
    private void prepareRecyclerView() {
        inventoryRecyclerView.setHasFixedSize(true);
        inventoryLayoutManager = new LinearLayoutManager(getContext());
        inventoryAdapter = new ShoppingAdapter(SingletonDatabaseManager.getInstance(getContext()).getItensShopping());
        inventoryRecyclerView.setLayoutManager(inventoryLayoutManager);
        inventoryRecyclerView.setAdapter(inventoryAdapter);
    }


    /*Edit Values Dialog*/
    public void editInventoryDialog() {
        LayoutInflater inflater = this.getLayoutInflater();
        View titleView = inflater.inflate(R.layout.alert_dialog_add_shopping_item_title, null);

        final AlertDialog diag = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme)
                .setCustomTitle(titleView)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    ShoppingItem item = new ShoppingItem();
                    public void onClick(DialogInterface dialog, int id) {
                        item.setProductName(addItem.getText().toString());
                        item.setProductImage("");

                        SingletonDatabaseManager.getInstance(getContext()).adicionarItemShopping(item);
                        Toast.makeText(getContext(), addItem.getText(), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(Html.fromHtml("<font color='#FEB117'><strong>Cancel</font>"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setView(R.layout.alert_dialog_add_shopping_item_body)
                .create();
        diag.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(diag.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        diag.getWindow().setAttributes(lp);

        /*Find views By Id / Set Text*/
        addItem = diag.findViewById(R.id.nomeProduct);
        addImagem = diag.findViewById(R.id.btn_adicionar_imagem);
        imageView = diag.findViewById(R.id.imagem);
        addImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission((getActivity()), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        String [] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, REQUEST_CODE_STORAGE_PERMISSION);
                    }
                    else {
                        selecionarImagem();
                    }
                }
                else {
                    selecionarImagem();
                }
            }


        });

        tirarFoto = diag.findViewById(R.id.btn_tirar_foto);
        tirarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission((getActivity()), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
                        String [] permissions = {Manifest.permission.CAMERA};
                        requestPermissions(permissions, REQUEST_CODE_CAMERA);
                    }
                    else {
                        tirarFoto();
                    }
                }
                else {
                    tirarFoto();
                }

            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        imageView.setImageBitmap(selectedImage);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage =  data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                byte[] byteArray = cursor.getBlob(columnIndex);
                                imgbt = BitmapFactory.decodeByteArray(byteArray, 0 ,byteArray.length);

                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }
    }
    @Override
    public void onChangeList(ArrayList<ShoppingItem> itens) {
        Log.d("ShoppingListFragment", "onChangeList called");
        inventoryAdapter = new ShoppingAdapter(SingletonDatabaseManager.getInstance(getContext()).getItensShopping());
        inventoryRecyclerView.setAdapter(inventoryAdapter);

        inventoryAdapter.setOnItemClickListener(new ShoppingAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                SingletonDatabaseManager.getInstance(getContext()).deleteItemShopping(inventoryAdapter.InventoryList.get(position).getIditem());
            }
        });
    }
}