#include <iostream>
#include <string>

using namespace std;

class Date {
public:
    Date();

    Date(const string &date);

    const string &getDate() const;

    void setDate(const string &date);

    void daysLater(int day);

    void daysBefore(int day);

    int d_to_int(string date);

    static string int_to_date(int day);


    static bool leap_year(int year) {
        return ((year % 400 == 0) || (year % 100 != 0 && year % 4 == 0));
    }

    static int date_to_int(string date) {
        int year, month, day;
        sscanf(date.c_str(), "%d-%d-%d", &year, &month, &day);
        int month_length[] = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int ans = day - 1;
        while (month != 0) {
            --month;
            ans += month_length[month];
            if (month == 2 && leap_year(year))
                ans += 1;
        }
        ans += 365 * (year - 1970);
        ans += (year - 1) / 4 - 1970 / 4;
        ans -= (year - 1) / 100 - 1970 / 100;
        ans += (year - 1) / 400 - 1970 / 400;
        return ans;
    }

    static int daysBetweenDates(Date date1, Date date2) {
        string d1 = date1.getDate();
        string d2 = date2.getDate();
        return abs(date_to_int(d1) - date_to_int(d2));
    }

    static void printTwoDateBetween() {
        cout << "请输入两个日期" << endl;
        string d1;
        string d2;
        cin >> d1;
        cin >> d2;
        Date date1(d1);
        Date date2(d2);
        int days_between = Date::daysBetweenDates(date1, date2);
        cout << "日期间隔为： " << days_between << " 天" << endl;
    }

    static void printDateLater() {
        cout << "请输入一个日期" << endl;
        string d;
        cin >> d;
        Date date(d);
        cout << "现在的日期是: " << d << endl;
        int dayLater = 0;
        cout << "输入距现在多少天后： " << endl;
        cin >> dayLater;
        date.daysLater(dayLater);
    }

    static void printDateBefore() {
        cout << "请输入一个日期" << endl;
        string d;
        cin >> d;
        Date date(d);
        cout << "现在的日期是: " << d << endl;
        int dayBefore = 0;
        cout << "输入距现在多少天前： " << endl;
        cin >> dayBefore;
        date.daysBefore(dayBefore);
    }


private:
    string date;

};

Date::Date(const string &date) : date(date) {}

const string &Date::getDate() const {
    return date;
}

void Date::setDate(const string &date) {
    Date::date = date;
}

void Date::daysLater(int day) {
    int total_days = date_to_int(getDate()) + day;
    string d = int_to_date(total_days);
    cout << day << "天后是： " << d << endl;
}

void Date::daysBefore(int day) {
    int total_days = date_to_int(getDate()) - day;
    string d = int_to_date(total_days);
    cout << day << "天前是： " << d << endl;
}

string Date::int_to_date(int day) {
    bool flag = false;
    int year = day / 365;
    int day_left = day % 365;
    for (int i = 0; i < year; i++) {
        int current_year = 1970 + i;
        if ((current_year % 400 == 0) || (current_year % 100 != 0 && current_year % 4 == 0)) {
            day_left--;
        }
    }
    if (day_left < 0) {
        year--;
        day_left += 365;
    }
    //计算年月日
    int year_now = 1970 + year;
    int month_now = 0;
    int day_now = 1;
    if ((year_now % 400 == 0) || (year_now % 100 != 0 && year_now % 4 == 0)) {
        flag = true;
    }
    int month_length[] = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    if (flag) {
        month_length[2] = 29;
    }
    for (int i = 1; i <= 12; i++) {
        if (day_left >= month_length[i]) {
            day_left -= month_length[i];
        } else {
            month_now = i;
            day_now += day_left;
            break;
        }
    }
    string res = to_string(year_now) + "-" + to_string(month_now) + "-" + to_string(day_now);
    return res;
}

int Date::d_to_int(string date) {
    int year, month, day;
    sscanf(date.c_str(), "%d-%d-%d", &year, &month, &day);
    int month_length[] = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    int ans = day - 1;
    while (month != 0) {
        --month;
        ans += month_length[month];
        if (month == 2 && leap_year(year))
            ans += 1;
    }
    ans += 365 * (year - 1970);
    ans += (year - 1) / 4 - 1970 / 4;
    ans -= (year - 1) / 100 - 1970 / 100;
    ans += (year - 1) / 400 - 1970 / 400;
    return ans;

}


int main() {

    cout << "\t输入 1 计算两个日期之差\n \t输入 2 计算给定日期后给定天数的日期\n\t输入 3 计算给定日期前给定天数的日期\n(q to quit)"<<endl;
    int input_order = 0;
    cin >> input_order;
    switch (input_order){
        case 1:
            Date::printTwoDateBetween();
            break;
        case 2:
            Date::printDateLater();
            break;
        case 3:
            Date::printDateBefore();
            break;
        default:
            break;
    }

}