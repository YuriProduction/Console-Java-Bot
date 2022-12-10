package InvestHelper;

public class StringFormater implements FormatableString {

  @Override
  public String format(String companyAndData) {
//    String company = companyAndData.split("_")[0];
//    String date = companyAndData.split("_")[1];
//    String month = date.substring(4, 7);
//    String time = date.substring(11,19);
//    String number = date.substring(8, 10);
//    String year = date.substring(date.length() - 4);
//    return company + " " + month + " " + number + " " + time + " " + year;
      return companyAndData;
  }
}


interface FormatableString {

  public String format(String someString);
}


