public class Logger {
    /** Создание объекта Singleton - контроль единственности экземпляра */
    public static Logger getInstance()
    {
        if (LogInstance == null) {
            LogInstance = new Logger();
        }
        return LogInstance;
    }

    void writeLog(String plogText,int pLoglevel){
    }
    /** Изменение уровня подробности журналирования */
    void setLogLevel(int pLoglevel){
    }
    /** Возобновить запись в журнал */
    void enableLog(){
    }
    /** Приостановить запись в журнал без закрытия файла */
    void disableLog(){
    }
    /** Отключить запись в журнал - с закрытием файла*/
    void stopLog(){
    }
    /** Отключить запись в журнал - с закрытием файла*/
    void resumeLog(){
    }
    /** Очистить файл*/
    void clearlogFile(){
    }


    private static Logger LogInstance;

    /** Конструкторы Logger - Singleton - приватные, чтобы было невозможно создат 2 экземпляр */
    private Logger() {


    }


}
