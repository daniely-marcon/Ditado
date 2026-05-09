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

                dao.insert(new Animal("Cobra-Coral", drawableToByteArray(mContext, R.drawable.coral),  "Répteis"));
                dao.insert(new Animal("Iguana", drawableToByteArray(mContext, R.drawable.iguana), "Répteis"));
                dao.insert(new Animal("Sucuri", drawableToByteArray(mContext, R.drawable.sucuri), "Répteis"));
                dao.insert(new Animal("Tartaruga", drawableToByteArray(mContext, R.drawable.tartaruga), "Répteis"));
                dao.insert(new Animal("Lagartixa", drawableToByteArray(mContext, R.drawable.lagartixa), "Répteis"));

                dao.insert(new Animal("Arara", drawableToByteArray(mContext, R.drawable.arara), "Aves"));
                dao.insert(new Animal("Tucano", drawableToByteArray(mContext, R.drawable.tucano), "Aves"));
                dao.insert(new Animal("Beija-Flor", drawableToByteArray(mContext, R.drawable.beijaflor), "Aves"));
                dao.insert(new Animal("Papagaio", drawableToByteArray(mContext, R.drawable.papagaio), "Aves"));
                dao.insert(new Animal("Rolinha", drawableToByteArray(mContext, R.drawable.rolinha), "Aves"));




                dao.insert(new Animal("Pirarucu", drawableToByteArray(mContext, R.drawable.pirarucu), "Peixes"));
                dao.insert(new Animal("Piau", drawableToByteArray(mContext, R.drawable.piau), "Peixes"));
                dao.insert(new Animal("Dourado", drawableToByteArray(mContext, R.drawable.dourado),  "Peixes"));
                dao.insert(new Animal("Piraputanga", drawableToByteArray(mContext, R.drawable.piraputanga), "Peixes"));
                dao.insert(new Animal("Bagre", drawableToByteArray(mContext, R.drawable.bagre), "Peixes"));


                dao.insert(new Animal("Sapo", drawableToByteArray(mContext, R.drawable.sapo), "Anfíbios"));
                dao.insert(new Animal("Axolote", drawableToByteArray(mContext, R.drawable.axolote),  "Anfíbios"));
                dao.insert(new Animal("Perereca", drawableToByteArray(mContext, R.drawable.perereca), "Anfíbios"));
                dao.insert(new Animal("Salamandra", drawableToByteArray(mContext, R.drawable.salamandra), "Anfíbios"));


                dao.insert(new Animal("Cachorro", drawableToByteArray(mContext, R.drawable.cachorro),  "Mamíferos"));
                dao.insert(new Animal("Vaca", drawableToByteArray(mContext, R.drawable.vaca), "Mamíferos"));
                dao.insert(new Animal("Gato", drawableToByteArray(mContext, R.drawable.gato),  "Mamíferos"));
                dao.insert(new Animal("Baleia", drawableToByteArray(mContext, R.drawable.baleia),  "Mamíferos"));
                dao.insert(new Animal("Elefante", drawableToByteArray(mContext, R.drawable.elefante),  "Mamíferos"));

            });
        }
    };

    private static byte[] drawableToByteArray(Context context, int resId) {
        try {
            android.graphics.drawable.Drawable drawable = androidx.core.content.ContextCompat.getDrawable(context, resId);
            if (drawable == null) return new byte[0];

            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();


            if (width > 300) {
                float ratio = (float) height / width;
                width = 300;
                height = (int) (width * ratio);
            }
            if (width <= 0) width = 150;
            if (height <= 0) height = 150;

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            android.graphics.Canvas canvas = new android.graphics.Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();


            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            return stream.toByteArray();

        } catch (Exception e) {

            e.printStackTrace();
            return new byte[0];
        }
    }


}