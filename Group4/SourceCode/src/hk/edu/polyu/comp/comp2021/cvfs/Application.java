    package hk.edu.polyu.comp.comp2021.cvfs;


    import hk.edu.polyu.comp.comp2021.cvfs.view.CLIView;

    /**
     * The application class serves as the entry ppoint for the program
     */
    public class Application {
        /**
         * The main method that starts the command-line interface (CLI)
         *
         * @param args the command-line arguments
         */
        public static void main(String[] args) {
            CLIView view = new CLIView();
            view.start();
        }
    }