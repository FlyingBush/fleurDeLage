package fr.fleurdelage.fleurdelage.tellowsdb;

import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fr.fleurdelage.fleurdelage.R;

@Database(entities = {PhoneNumber.class}, version = 1, exportSchema = false)
public abstract class PhoneNumberRoomDataBase extends RoomDatabase {
    public abstract PhoneNumberDao getPhoneNumberDao();

    private static volatile PhoneNumberRoomDataBase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static PhoneNumberRoomDataBase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PhoneNumberRoomDataBase.class) {
                if (INSTANCE == null) {

                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PhoneNumberRoomDataBase.class, "phone_number_database")
                            .allowMainThreadQueries()
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);

                                    databaseWriteExecutor.execute( () -> {

                                        PhoneNumberDao dao = INSTANCE.getPhoneNumberDao();
                                        List<PhoneNumber> list = dao.getAll();

                                        if (list.isEmpty()) {
                                            Resources resources = context.getResources();
                                            InputStream inputStream = resources.openRawResource(R.raw.tellows_db);
                                            Reader reader = new InputStreamReader(inputStream);

                                            CsvToBean<PhoneNumber> csv = new CsvToBeanBuilder<PhoneNumber>(reader)
                                                    .withType(PhoneNumber.class)
                                                    .withSeparator(';')
                                                    .build();

                                            Iterator<PhoneNumber> iterator = csv.iterator();
                                            List<PhoneNumber> phoneNumbers = new ArrayList<>();

                                            int i=0;
                                            while (iterator.hasNext()) {
                                                PhoneNumber buffer = iterator.next();
                                                phoneNumbers.add(buffer);
                                                System.out.println("Add line " + i);
                                                i++;
                                            }

                                            dao.insertAll(phoneNumbers);
                                            System.out.println(phoneNumbers);
                                        }
                                    });
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
