package TaskTraker;

class Managers {
    //public static TaskManager getDefault2 (){return new InMemoryTaskManager();}
    public static TaskManager getDefault (){
        return new FileBackedTaskManager("backup.csv");
    }
    public static HistoryManager getDefaultHistory (){
        return new InMemoryHistoryManager();
    }
}
