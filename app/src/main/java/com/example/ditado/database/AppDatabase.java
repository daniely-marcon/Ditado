package com.example.ditado.database;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.ditado.R;
import com.example.ditado.dao.AnimalDao;
import com.example.ditado.dao.PalavrasAprendidasDao;
import com.example.ditado.dao.UsuarioDao;
import com.example.ditado.entities.Animal;
import com.example.ditado.entities.PalavrasAprendidas;
import com.example.ditado.entities.Usuario;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executors;

@Database(entities = {Usuario.class, Animal.class, PalavrasAprendidas.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract AnimalDao animalDao();
    public abstract PalavrasAprendidasDao palavrasAprendidasDao();
    public abstract UsuarioDao usuarioDao();

    private static volatile AppDatabase INSTANCE;
    private static Context mContext;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    mContext = context.getApplicationContext();
                    INSTANCE = Room.databaseBuilder(mContext,
                                    AppDatabase.class, "database")
                            .addCallback(sRoomDatabaseCallback)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Executors.newSingleThreadExecutor().execute(() -> {
                AnimalDao dao = INSTANCE.animalDao();

                dao.insert(new Animal("Arara", drawableToByteArray(mContext, R.drawable.arara), rawToByteArray(mContext, R.raw.arara), "Aves"));
                dao.insert(new Animal("Tucano", drawableToByteArray(mContext, R.drawable.tucano), rawToByteArray(mContext, R.raw.tucano), "Aves"));
                dao.insert(new Animal("Beija-Flor", drawableToByteArray(mContext, R.drawable.beijaflor), rawToByteArray(mContext, R.raw.beijaflor), "Aves"));
                dao.insert(new Animal("Papagaio", drawableToByteArray(mContext, R.drawable.papagaio), rawToByteArray(mContext, R.raw.papagaio), "Aves"));
                dao.insert(new Animal("Rolinha", drawableToByteArray(mContext, R.drawable.rolinha), rawToByteArray(mContext, R.raw.rolinha), "Aves"));


                dao.insert(new Animal("Cobra-Coral", drawableToByteArray(mContext, R.drawable.coral), rawToByteArray(mContext, R.raw.cobracoral), "Répteis"));
                dao.insert(new Animal("Iguana", drawableToByteArray(mContext, R.drawable.iguana), rawToByteArray(mContext, R.raw.iguana), "Répteis"));
                dao.insert(new Animal("Sucuri", drawableToByteArray(mContext, R.drawable.sucuri), rawToByteArray(mContext, R.raw.sucuri), "Répteis"));
                dao.insert(new Animal("Tartaruga", drawableToByteArray(mContext, R.drawable.tartaruga), rawToByteArray(mContext, R.raw.tartaruga), "Répteis"));
                dao.insert(new Animal("Lagartixa", drawableToByteArray(mContext, R.drawable.lagartixa), rawToByteArray(mContext, R.raw.lagartixa), "Répteis"));


                dao.insert(new Animal("Pirarucu", drawableToByteArray(mContext, R.drawable.pirarucu), rawToByteArray(mContext, R.raw.pirarucu), "Peixes"));
                dao.insert(new Animal("Piau", drawableToByteArray(mContext, R.drawable.piau), rawToByteArray(mContext, R.raw.piau), "Peixes"));
                dao.insert(new Animal("Dourado", drawableToByteArray(mContext, R.drawable.dourado), rawToByteArray(mContext, R.raw.dourado), "Peixes"));
                dao.insert(new Animal("Piraputanga", drawableToByteArray(mContext, R.drawable.piraputanga), rawToByteArray(mContext, R.raw.piraputanga), "Peixes"));
                dao.insert(new Animal("Bagre", drawableToByteArray(mContext, R.drawable.bagre), rawToByteArray(mContext, R.raw.bagre), "Peixes"));


                dao.insert(new Animal("Sapo", drawableToByteArray(mContext, R.drawable.sapo), rawToByteArray(mContext, R.raw.sapo), "Anfíbios"));
                dao.insert(new Animal("Axolote", drawableToByteArray(mContext, R.drawable.axolote), rawToByteArray(mContext, R.raw.axolote), "Anfíbios"));
                dao.insert(new Animal("Perereca", drawableToByteArray(mContext, R.drawable.perereca), rawToByteArray(mContext, R.raw.perereca), "Anfíbios"));
                dao.insert(new Animal("Salamandra", drawableToByteArray(mContext, R.drawable.salamandra), rawToByteArray(mContext, R.raw.salamandra), "Anfíbios"));


                dao.insert(new Animal("Cachorro", drawableToByteArray(mContext, R.drawable.cachorro), rawToByteArray(mContext, R.raw.cachorro), "Mamíferos"));
                dao.insert(new Animal("Vaca", drawableToByteArray(mContext, R.drawable.vaca), rawToByteArray(mContext, R.raw.vaca), "Mamíferos"));
                dao.insert(new Animal("Gato", drawableToByteArray(mContext, R.drawable.gato), rawToByteArray(mContext, R.raw.gato), "Mamíferos"));
                dao.insert(new Animal("Baleia", drawableToByteArray(mContext, R.drawable.baleia), rawToByteArray(mContext, R.raw.baleia), "Mamíferos"));
                dao.insert(new Animal("Elefante", drawableToByteArray(mContext, R.drawable.elefante), rawToByteArray(mContext, R.raw.elefante), "Mamíferos"));

            });
        }
    };

    // Métodos auxiliares de conversão (Necessários para transformar recursos em byte[])
    private static byte[] drawableToByteArray(Context context, int resId) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private static byte[] rawToByteArray(Context context, int resId) {
        try {
            InputStream is = context.getResources().openRawResource(resId);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            return buffer;
        } catch (IOException e) {
            return new byte[0];
        }
    }
}