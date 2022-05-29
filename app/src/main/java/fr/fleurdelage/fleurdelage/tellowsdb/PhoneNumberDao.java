package fr.fleurdelage.fleurdelage.tellowsdb;

import static androidx.room.OnConflictStrategy.IGNORE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PhoneNumberDao {
    @Insert(onConflict = IGNORE)
    void insert(PhoneNumber phoneNumber);

    @Insert(onConflict = IGNORE)
    void insertAll(List<PhoneNumber> phoneNumbers);

    @Delete
    void delete(PhoneNumber phoneNumber);

    @Query("DELETE FROM phone_numbers_table WHERE phone_number = :phoneNumber")
    void delete(String phoneNumber);

    @Query("DELETE FROM phone_numbers_table")
    void removeAll();

    @Query("SELECT * FROM phone_numbers_table")
    List<PhoneNumber> getAll();

    @Query("SELECT * FROM phone_numbers_table WHERE phone_number = :phoneNumber LIMIT 1")
    PhoneNumber get(String phoneNumber);
}
