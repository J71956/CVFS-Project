    package hk.edu.polyu.comp.comp2021.cvfs.model;

    import java.io.Serializable;

    /**
     * This class represents a document in a virtual file system
     */
    public class Document implements Serializable {
        /**
         * The base size of the document is set
         */
        public static final int size = 40;
        private String name;
        private String type;
        private String content;

        /**
         * Contructs a new document with the following specified parameters
         *
         * @param name name of the document
         * @param type name of the document
         * @param content content of the document
         */
        public Document(String name, String type, String content) {
            this.name = name;
            this.type = type;
            this.content = content;
        }

        /**
         * @return the name of the document
         */
        public String getName() {
            return name;
        }

        /**
         * @param name the new desired name of the document
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return the type of document
         */
        public String getType() {
            return type;
        }

        /**
         * @return the content of the document
         */
        public String getContent() {
            return content;
        }

        /**
         * @return the size of the document
         */
        public int getSize() {
            return size + content.length() * 2;
        }


    }