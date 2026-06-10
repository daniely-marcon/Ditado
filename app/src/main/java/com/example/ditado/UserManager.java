package com.example.ditado;

import android.content.Context;
import com.example.ditado.database.AppDatabase;
import com.example.ditado.entities.Usuario;
import com.example.ditado.security.SecurityUtils;
import java.util.List;

public class UserManager {
    private final com.example.ditado.dao.UsuarioDao usuarioDao;

    public UserManager(Context context) {
        AppDatabase db = AppDatabase.getDatabase(context);
        this.usuarioDao = db.usuarioDao();
    }


    public void registerUser(String nome, String email, String password, String tipo, byte[] foto) {
        new Thread(() -> {
            try {
                String hashed = SecurityUtils.hashPassword(password);

                Usuario usuario = new Usuario(nome, email, hashed, foto, tipo);

                usuarioDao.insert(usuario);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void updateUser(int idUsuario, String newNome, String newEmail, String newPassword, String newTipo, byte[] newFoto, OnUpdateCallback callback) {
        new Thread(() -> {
            Usuario usuario = usuarioDao.getUsuarioById(idUsuario);
            if (usuario == null) {
                usuario = usuarioDao.buscarUsuario(newEmail);
            }

            if (usuario != null) {
                if (!newNome.isEmpty()) usuario.setNome_usuario(newNome);
                if (!newEmail.isEmpty()) usuario.setEmail(newEmail);
                if (!newTipo.isEmpty()) usuario.setTipo(newTipo);
                if (newFoto != null) usuario.setImagem_usuario(newFoto);

                if (!newPassword.isEmpty()) {
                    usuario.setSenha(SecurityUtils.hashPassword(newPassword));
                }

                usuarioDao.update(usuario);

                // AVISA QUE TERMINOU DE SALVAR NO BANCO
                if (callback != null) {
                    callback.onUpdateFinished();
                }
            }
        }).start();
    }

    public void getUsuario(int idUsuario, UsuarioCallback callback) {
        new Thread(() -> {
            Usuario usuario = usuarioDao.getUsuarioById(idUsuario);
            callback.onUsuarioLoaded(usuario);
        }).start();
    }

    public void buscarUsuarioPorEmail(String email, UsuarioByEmailCallback callback) {
        new Thread(() -> {
            Usuario usuario = usuarioDao.buscarUsuario(email);
            callback.onUsuarioLoaded(usuario);
        }).start();
    }
    public void redefinirSenhaPorEmail(String email, String novaSenhaPura, OnResetPasswordCallback callback) {
        new Thread(() -> {
            try {

                Usuario usuario = usuarioDao.buscarUsuario(email);

                if (usuario != null) {

                    String senhaComHash = SecurityUtils.hashPassword(novaSenhaPura);
                    usuario.setSenha(senhaComHash);


                    usuarioDao.update(usuario);
                    callback.onResult(true, null);
                } else {
                    callback.onResult(false, "Usuário não encontrado.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                callback.onResult(false, e.getMessage());
            }
        }).start();
    }

    public interface OnResetPasswordCallback {
        void onResult(boolean sucesso, String erroMensagem);
    }
    public interface UsuarioByEmailCallback {
        void onUsuarioLoaded(Usuario usuario);
    }
    public interface UsuarioCallback {
        void onUsuarioLoaded(Usuario usuario);
    }

    public interface OnUpdateCallback {
        void onUpdateFinished();
    }



}