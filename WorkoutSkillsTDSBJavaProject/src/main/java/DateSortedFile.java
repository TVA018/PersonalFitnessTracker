public class DateSortedFile {
    String date;
    String id;
    String fileData;
    int dateValue;

    public DateSortedFile(String date, String id, String fileData){
        this.date = date;
        this.id = id;
        this.fileData = fileData;
        String[] splitDate = date.split("-");
        int day = Integer.parseInt(splitDate[0]);
        int month = Integer.parseInt(splitDate[1]);
        int year = Integer.parseInt(splitDate[2]);
        this.dateValue = day + month * 100 + year * 10000;
    }
}
