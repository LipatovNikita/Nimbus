package Beans;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import java.io.*;
import java.security.SecureRandom;


/**
 * Created by tanya on 09.01.2017.
 */
@ManagedBean
@SessionScoped
public class UploadBean {

    private  String destination= System.getProperty("user.home");
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();
    String name;

    public String upload(FileUploadEvent event) {
        FacesMessage msg = new FacesMessage("Success! ", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, msg);

        try {
            copyFile(event.getFile().getFileName(), event.getFile().getInputstream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return name;
    }

    public String copyFile(String fileName, InputStream in) {
        name = renameFile();
        try {

            OutputStream out = new FileOutputStream(new File(destination+"\\images\\" + name));
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            in.close();
            out.flush();
            out.close();
            System.out.println("New file created!");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return name;
    }


    public String renameFile(){
        int len=12;
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }

    public StreamedContent getImage() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {

            return new DefaultStreamedContent();
        }
        else {
            String home_user = System.getProperty("user.home")+"\\images\\";
            String path = home_user+context.getExternalContext().getRequestParameterMap().get("path");
            FileInputStream fin = new FileInputStream(path);
            System.out.println(path);
            byte[] image = new byte[fin.available()];
            fin.read(image, 0, fin.available());

            StreamedContent content = new DefaultStreamedContent(new ByteArrayInputStream(image));
            return content;
        }
    }
}
