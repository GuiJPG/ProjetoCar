package com.example.projetocar.activitys;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.example.projetocar.R;
import com.example.projetocar.helper.FirebaseHelper;
import com.example.projetocar.helper.Mascara;
import com.example.projetocar.model.Automovel;
import com.example.projetocar.model.Categoria;
import com.example.projetocar.model.Endereco;
import com.example.projetocar.model.Imagem;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.santalu.maskara.widget.MaskEditText;
import com.squareup.picasso.Picasso;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FormCarroActivity extends AppCompatActivity {

    private ImageView img0;
    private ImageView img1;
    private ImageView img2;

    private EditText edt_titulo;
    private CurrencyEditText edt_valor_comprado;
    private EditText edt_placa;
    private Button btn_categoria;
    private EditText edt_modelo;
    private EditText edt_ano_modelo;
    private EditText edt_quilometragem;
    private EditText edt_descricao;
    private Button btn_endereco;
    private TextView txt_endereco;

    private TextView text_toolbar;


    private Endereco endereco;

    private String currentPhotoPath;


    private String categoriaSelecionada = "";
    private String enderecoSelecionado = "";

    private final int REQUEST_CATEGORIA = 100;
    private final int REQUEST_ENDERECO = 200;

    private List<Imagem> imagemList = new ArrayList<>();

    private Automovel automovel;
    private boolean novoAnuncio = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_carro);

        iniciarComponesntes();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            automovel = (Automovel) bundle.getSerializable("automovelSelecionado");

            configDados();
        }

        configCliques();

        recuperaEndereco();

    }

    private void configDados() {
        text_toolbar.setText("Editando Automovel");

        categoriaSelecionada = automovel.getCategoria();
        btn_categoria.setText(categoriaSelecionada);

        edt_titulo.setText(automovel.getTitulo());
        edt_valor_comprado.setText(Mascara.getValor(automovel.getValor()));
        edt_placa.setText(automovel.getPlaca());
        edt_modelo.setText(automovel.getModelo());
        edt_ano_modelo.setText(automovel.getAnoModelo());
        edt_quilometragem.setText(automovel.getQuilometragem());
        edt_descricao.setText(automovel.getDescricao());

        // Implementar metodo para recuperar endereço

        //enderecoSelecionado = endereco.getBairro();
        //btn_endereco.setText(enderecoSelecionado);

        Picasso.get().load(automovel.getUrlImagens().get(0)).into(img0);
        Picasso.get().load(automovel.getUrlImagens().get(1)).into(img1);
        Picasso.get().load(automovel.getUrlImagens().get(2)).into(img2);

        novoAnuncio = false;


    }


    private void recuperaEndereco(){
        if(FirebaseHelper.getAutenticado()){
            DatabaseReference enderecoRef = FirebaseHelper.getDatabaseReference()
                    .child("enderecos")
                    .child(FirebaseHelper.getIdFirebase());

            enderecoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        for(DataSnapshot ds : snapshot.getChildren()){
                            endereco = ds.getValue(Endereco.class);
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }

    public void validaDados(View view){

        String titulo = edt_titulo.getText().toString();
        double valor = (double) edt_valor_comprado.getRawValue() / 100;
        String placa = edt_placa.getText().toString();
        String modelo = edt_modelo.getText().toString();
        String anoModelo = edt_ano_modelo.getText().toString();
        String quilometragem = edt_quilometragem.getText().toString();
        String descricao = edt_descricao.getText().toString();


       if(!titulo.isEmpty()){
           if(valor > 0){
               if(placa.length() == 7){
                       if(!modelo.isEmpty()){
                           if(anoModelo.length() == 4){
                               if(!quilometragem.isEmpty()){
                                   if(!descricao.isEmpty()){
                                           if(!categoriaSelecionada.isEmpty()){
                                              if(endereco.getBairro() != null){

                                                if(automovel == null) automovel = new Automovel();
                                                automovel.setIdUsuario(FirebaseHelper.getIdFirebase());
                                                automovel.setTitulo(titulo);
                                                automovel.setValor(valor);
                                                automovel.setPlaca(placa);
                                                automovel.setCategoria(categoriaSelecionada);
                                                automovel.setModelo(modelo);
                                                automovel.setAnoModelo(anoModelo);
                                                automovel.setQuilometragem(quilometragem);
                                                automovel.setDescricao(descricao);
                                                automovel.setEndereco(endereco);

                                                if(novoAnuncio){
                                                    if(imagemList.size() == 3){
                                                        for(int i = 0; i < imagemList.size(); i++){
                                                            salvarImagemFirebase(imagemList.get(i), i);
                                                        }
                                                    }else{
                                                        Toast.makeText(this, "Selecione 3 Imagens", Toast.LENGTH_SHORT).show();
                                                    }
                                                }else{
                                                    if(imagemList.size() > 0){
                                                        for(int i = 0; i < imagemList.size(); i++){
                                                            salvarImagemFirebase(imagemList.get(i), i);
                                                        }

                                                    }else{
                                                        automovel.salvar(this, false);
                                                    }

                                                }

                                               //Adicionar mensagem de finalizado


                                              }else{
                                                  Toast.makeText(this, "Selecione um endereço", Toast.LENGTH_SHORT).show();
                                              }

                                           }else{
                                               Toast.makeText(this, "Selecione uma categoria", Toast.LENGTH_SHORT).show();

                                           }


                                   }else{
                                       edt_descricao.requestFocus();
                                       edt_descricao.setError("Informe uma descrição");
                                   }

                               }else{
                                   edt_quilometragem.requestFocus();
                                   edt_quilometragem.setError("Informe a quilometragem");
                               }

                           }else{
                               edt_ano_modelo.requestFocus();
                               edt_ano_modelo.setError("Informe ano do carro");
                           }

                       }else{
                           edt_modelo.requestFocus();
                           edt_modelo.setError("Informe um Modelo");
                       }

               }else{
                   edt_placa.requestFocus();
                   edt_placa.setError("Placa invalida");
               }

           }else{
               edt_valor_comprado.requestFocus();
               edt_valor_comprado.setError("Informe um valor válido");
           }

       }else{
           edt_titulo.requestFocus();
           edt_titulo.setError("Informe um titulo");
       }
    }

    private void salvarImagemFirebase(Imagem imagem, int index){
        StorageReference storageReference = FirebaseHelper.getStorageReference()
                .child("imagens")
                .child("anuncios")
                .child(automovel.getId())
                .child("imagem" + index + ".jpeg");

        UploadTask uploadTask = storageReference.putFile(Uri.parse(imagem.getCaminhoimagem()));
        uploadTask.addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnCompleteListener(task -> {

            if(novoAnuncio){
                automovel.getUrlImagens().add(index,task.getResult().toString());
            }else{
                automovel.getUrlImagens().set(imagem.getIndex(), task.getResult().toString());
            }
            if(imagemList.size() == index + 1){
                automovel.salvar(this, novoAnuncio);
            }

        })).addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());
        //Salva as imagens como imagem0.jpeg......
    }

    public void selecionarCategoria(View view){
        Intent intent = new Intent(this, CategoriaActivity.class);
        startActivityForResult(intent, REQUEST_CATEGORIA);
    }

    public void selecionarEndereco(View view){
        Intent intent = new Intent(this, UsuarioSelecionaEnderecoActivity.class);
        startActivityForResult(intent, REQUEST_ENDERECO);

    }

    private void configCliques(){
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());

        img0.setOnClickListener(v -> showBottomDialog(0));
        img1.setOnClickListener(v -> showBottomDialog(1));
        img2.setOnClickListener(v -> showBottomDialog(2));
    }

    private void configUpload(int requestCode, String caminhoImagem){

        int request = 0;

        switch (requestCode){
            case 0:
            case 3:
                request = 0;
                break;
            case 1:
            case 4:
                request = 1;
                break;
            case 2:
            case 5:
                request =  2;
                break;
        }
        Imagem imagem = new Imagem(caminhoImagem, request);
        if(imagemList.size() > 0){

            boolean encontrou = false;

            for (int i = 0; i <imagemList.size() ; i++) {
                if(imagemList.get(i).getIndex() == request){
                    encontrou = true;
                }
            }
            if(encontrou){
                imagemList.set(request, imagem);
            }else{
                imagemList.add(imagem);
            }

        }else{
            imagemList.add(imagem);
        }
    }

    public void showBottomDialog(int requestCode){
        View modalbottomsheet = getLayoutInflater().inflate(R.layout.layout_bottom_sheet, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialog);
        bottomSheetDialog.setContentView(modalbottomsheet);
        bottomSheetDialog.show();

        modalbottomsheet.findViewById(R.id.btn_camera).setOnClickListener(v ->{
            bottomSheetDialog.dismiss();
            verificaPermissaoCamera(requestCode);
        });
        modalbottomsheet.findViewById(R.id.btn_galeria).setOnClickListener(v ->{
            bottomSheetDialog.dismiss();
            verificaPermissaoGaleria(requestCode);
        });
        modalbottomsheet.findViewById(R.id.btn_close).setOnClickListener(v ->{
            bottomSheetDialog.dismiss();
            Toast.makeText(this, "Fechando", Toast.LENGTH_SHORT).show();
        });
    }

    private void verificaPermissaoCamera(int requestCode){
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                dispatchTakePictureIntent(requestCode);
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(FormCarroActivity.this, "Permissão Negada", Toast.LENGTH_SHORT).show();
            }
        };
        showDialogPermissao(permissionListener, new String[]{Manifest.permission.CAMERA},
                "Você negou as permissões para acessar a câmera do dispositivo, deseja permitir ?");

    }

    private void verificaPermissaoGaleria(int requestCode){
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
            abrirGaleria(requestCode);
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(FormCarroActivity.this, "Permissão Negada", Toast.LENGTH_SHORT).show();
            }
        };
        showDialogPermissao(permissionListener, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                "Você negou as permissões para acessar a galeria do dispositivo, deseja permitir ?");
    }

    private void dispatchTakePictureIntent(int requestCode) {

        int request = 0;

        switch (requestCode){
            case 0:
                request = 3;
                break;
            case 1:
                request = 4;
                break;
            case 2:
                request =  5;
                break;
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create the File where the photo should go
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(this,
                    "com.example.projetocar.fileprovider",
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, request);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void abrirGaleria(int requestCode){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, requestCode);
    }

    private void showDialogPermissao(PermissionListener permissionListener, String[] permission, String msg){
        TedPermission.create()
                .setPermissionListener(permissionListener)
                .setDeniedTitle("Permissão Negada")
                .setDeniedMessage(msg)
                .setDeniedCloseButtonText("Não")
                .setGotoSettingButtonText("Sim")
                .setPermissions(permission)
                .check();
    }

    @SuppressLint("WrongViewCast")
    private void iniciarComponesntes(){
        text_toolbar = findViewById(R.id.text_toolbar);
        text_toolbar.setText("Novo Carro");

        edt_titulo = findViewById(R.id.edt_titulo);
        edt_valor_comprado = findViewById(R.id.edt_valor_comprado);
        edt_valor_comprado.setLocale(new Locale("PT", "br"));
        edt_placa = findViewById(R.id.edt_placa);
        btn_categoria = findViewById(R.id.btn_categoria);
        edt_modelo = findViewById(R.id.edt_modelo);
        edt_ano_modelo = findViewById(R.id.edt_ano_modelo);
        edt_quilometragem = findViewById(R.id.edt_quilometragem);
        edt_descricao = findViewById(R.id.edt_descricao);
        btn_endereco = findViewById(R.id.btn_endereco);
        txt_endereco = findViewById(R.id.txt_endereco);

        img0 = findViewById(R.id.img0);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(resultCode == RESULT_OK){

            Bitmap bitmap0;
            Bitmap bitmap1;
            Bitmap bitmap2;

            Uri imagemSelecionada = data.getData();
            String caminhoImagem;

            if(requestCode == REQUEST_CATEGORIA){
                Categoria categoria = (Categoria) data.getSerializableExtra("categoriaSelecionada");
                categoriaSelecionada = categoria.getNome();
                btn_categoria.setText(categoriaSelecionada);

            }else if(requestCode == REQUEST_ENDERECO){
                endereco = (Endereco) data.getSerializableExtra("enderecoSelecionado");
                enderecoSelecionado = endereco.getLogradouro();
                txt_endereco.setText(endereco.getBairro() + " " + endereco.getUf());
                btn_endereco.setText(enderecoSelecionado);

            }else if(requestCode <= 2){ //Galeria

                try{
                    caminhoImagem = imagemSelecionada.toString();

                    switch (requestCode){
                        case 0:
                            if(Build.VERSION.SDK_INT < 28){
                                bitmap0 = MediaStore.Images.Media.getBitmap(getContentResolver(), imagemSelecionada);
                            }else{
                                ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), imagemSelecionada);
                                bitmap0 = ImageDecoder.decodeBitmap(source);
                            }
                            img0.setImageBitmap(bitmap0);
                            break;
                        case 1:
                            if(Build.VERSION.SDK_INT < 28){
                                bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), imagemSelecionada);
                            }else{
                                ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), imagemSelecionada);
                                bitmap1 = ImageDecoder.decodeBitmap(source);
                            }
                            img1.setImageBitmap(bitmap1);
                            break;
                        case 2:
                            if(Build.VERSION.SDK_INT < 28){
                                bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), imagemSelecionada);
                            }else{
                                ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), imagemSelecionada);
                                bitmap2 = ImageDecoder.decodeBitmap(source);
                            }
                            img2.setImageBitmap(bitmap2);
                            break;

                    }

                    configUpload(requestCode, caminhoImagem);

                }catch (IOException e){
                    e.printStackTrace();
                }

            }else{ //Camera
                File file = new File(currentPhotoPath);

                caminhoImagem = String.valueOf(file.toURI());

                switch (requestCode){
                    case 3:
                        img0.setImageURI(Uri.fromFile(file));
                        break;
                    case 4:
                        img1.setImageURI(Uri.fromFile(file));
                        break;
                    case 5:
                        img2.setImageURI(Uri.fromFile(file));
                        break;
                }

                configUpload(requestCode, caminhoImagem);
            }
        }

    }

}