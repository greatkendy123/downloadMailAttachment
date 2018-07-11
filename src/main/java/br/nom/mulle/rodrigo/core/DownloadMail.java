package br.nom.mulle.rodrigo.core;

import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.search.*;
import java.util.Date;

public class DownloadMail {

    private String user;
    private String psswd;
    private String pathToSave;
    private ReadConfig readConfig;
    private Boolean onlySeen;
    private Date initialDate;
    private Date finalDate;

    private DownloadMail() {}

    private DownloadMail(DownloadMailBuilder builder) {
        this.user = builder.user;
        this.psswd = builder.psswd;
        this.pathToSave = builder.pathToSave;
        this.readConfig = builder.readConfig;
        this.onlySeen = builder.onlySeen;
        this.initialDate = builder.initialDate;
        this.finalDate = builder.finalDate;
    }

    public String getUser() {
        return user;
    }

    public String getPathToSave() {
        return pathToSave;
    }

    public ReadConfig getReadConfig() {
        return readConfig;
    }

    public Boolean getOnlySeen() {
        return onlySeen;
    }

    public Date getInitialDate() {
        return initialDate;
    }

    public Date getFinalDate() {
        return finalDate;
    }

    @Override
    public String toString() {
        return "DownloadMail{" +
                "user='" + user + '\'' +
                ", pathToSave='" + pathToSave + '\'' +
                ", readConfig=" + readConfig +
                ", onlySeen=" + onlySeen +
                ", initialDate=" + initialDate +
                ", finalDate=" + finalDate +
                '}';
    }

    public void run() {
        try {
            Session session = Session.getDefaultInstance(readConfig.getProperties(), null);
            Store store = session.getStore("imaps");

            store.connect(user, psswd);

            Folder inbox = store.getFolder("inbox");

            inbox.open(Folder.READ_WRITE);
            int messageCount = inbox.getMessageCount();

            SearchTerm searchTerm = getTerms();

            Message[] messages;
            if (searchTerm == null) messages = inbox.getMessages();
            else messages = inbox.search(searchTerm);

            for(Message message : messages) {
                getAttachment(message);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean getAttachment(Message message) {
        boolean salvo = true;

        try {

            String contentType = message.getContentType();

            if (contentType.contains("multipart")) {
                Multipart multiPart = (Multipart) message.getContent();
                int numberOfParts = multiPart.getCount();

                for (int partCount = 0; partCount < numberOfParts; partCount++) {
                    MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                    if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                        String fileName = part.getFileName();
                        part.saveFile(pathToSave + fileName);
                    }
                }
            }
            else {
                System.out.println("Não continha anexo");
                salvo = true;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            salvo = false;
        }

        return salvo;
    }

    private SearchTerm getTerms() {
        FlagTerm onlySeenTerm = null;
        ReceivedDateTerm afterTerm = null;
        ReceivedDateTerm beforeTerm = null;

        if(onlySeen != null) onlySeenTerm = new FlagTerm(new Flags(Flags.Flag.SEEN), onlySeen);

        if (initialDate != null) afterTerm = new ReceivedDateTerm(ComparisonTerm.GE, initialDate);

        if (finalDate != null) beforeTerm = new ReceivedDateTerm(ComparisonTerm.LE, finalDate);

        if (afterTerm != null && beforeTerm != null) {
            AndTerm datesTerm = new AndTerm(afterTerm, beforeTerm);

            if (onlySeenTerm != null) return new AndTerm(datesTerm, onlySeenTerm);
            return datesTerm;
        }
        else if (afterTerm != null) {
            if (onlySeenTerm != null) return new AndTerm(afterTerm, onlySeenTerm);
            return afterTerm;
        }

        else if (beforeTerm != null) {
            if (onlySeenTerm != null) return new AndTerm(beforeTerm, onlySeenTerm);
            return beforeTerm;
        }

        else if (onlySeenTerm != null) return onlySeenTerm;

        return null;
    }

    public static class DownloadMailBuilder {
        private String user;
        private String psswd;
        private String pathToSave;
        private ReadConfig readConfig;
        private Boolean onlySeen;
        private Date initialDate;
        private Date finalDate;

        public DownloadMailBuilder(String user, String psswd, String pathToSave) {
            this.user = user;
            this.psswd = psswd;
            if (pathToSave.endsWith("/")) this.pathToSave = pathToSave;
            else this.pathToSave = pathToSave + "/";
        }

        public DownloadMailBuilder configs(ReadConfig readConfig) {
            this.readConfig = readConfig;
            return this;
        }

        public DownloadMailBuilder onlySeen(Boolean onlySeen) {
            this.onlySeen = onlySeen;
            return this;
        }

        public DownloadMailBuilder initialDate(Date initialDate) {
            this.initialDate = initialDate;
            return this;
        }

        public DownloadMailBuilder finalDate(Date finalDate) {
            this.finalDate = finalDate;
            return this;
        }

        public DownloadMail build() {
            return new DownloadMail(this);
        }
    }

}
