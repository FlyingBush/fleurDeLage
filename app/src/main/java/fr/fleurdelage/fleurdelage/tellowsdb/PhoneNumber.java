package fr.fleurdelage.fleurdelage.tellowsdb;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.opencsv.bean.CsvBindByPosition;

@Entity(tableName = "phone_numbers_table")
public class PhoneNumber {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "phone_number")
    @CsvBindByPosition(position = 0)
    public String phoneNumber;

    @ColumnInfo(name = "score")
    @CsvBindByPosition(position = 1)
    public int score;

    @ColumnInfo(name = "evaluations_amount")
    @CsvBindByPosition(position = 2)
    public long evalAmount;

    @ColumnInfo(name = "country_prefix")
    @CsvBindByPosition(position = 3)
    public String countryPrefix;

    @ColumnInfo(name = "local_prefix")
    @CsvBindByPosition(position = 4)
    public String localPrefix;

    @ColumnInfo(name = "search_amount")
    @CsvBindByPosition(position = 5)
    public long searchAmount;

    @ColumnInfo(name = "call_type")
    @CsvBindByPosition(position = 6)
    public String callType;

    @ColumnInfo(name = "caller_name")
    @CsvBindByPosition(position = 7)
    public String callerName;

    @ColumnInfo(name = "local_prefix_details")
    @CsvBindByPosition(position = 8)
    public String localPrefixDetails;

    @ColumnInfo(name = "last_comment_date")
    @CsvBindByPosition(position = 9)
    public String lastCommentDate;

    @ColumnInfo(name = "deeplink")
    @CsvBindByPosition(position = 10)
    public String link;

    @ColumnInfo(name = "call_type_id")
    @CsvBindByPosition(position = 11)
    public int callerTypeId;
}
