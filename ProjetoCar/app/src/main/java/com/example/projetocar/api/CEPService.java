package com.example.projetocar.api;

import com.example.projetocar.model.Endereco;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CEPService {

    @GET("{cep}/json/")
    Call<Endereco> recuperaCEP(@Path("cep") String cep);
}
