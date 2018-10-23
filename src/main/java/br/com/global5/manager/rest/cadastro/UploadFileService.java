package br.com.global5.manager.rest.cadastro;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import javax.imageio.ImageIO;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.*;


@Path("/file")
public class UploadFileService {

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail,
            @FormDataParam("path") String path) {


        // Path format //10.217.14.97/Installables/uploaded/
        System.out.println("path::"+path);
        String uploadedFileLocation = path
                + fileDetail.getFileName();

        // save it
        writeToFile(uploadedInputStream, uploadedFileLocation);

        String output = "File uploaded to : " + uploadedFileLocation;

        return Response.status(200).entity(output).build();

    }

    // save uploaded file to new location
    private void writeToFile(InputStream uploadedInputStream,
                             String uploadedFileLocation) {

        try {
            OutputStream out = new FileOutputStream(new File(
                    uploadedFileLocation));
            int read = 0;
            byte[] bytes = new byte[1024];

            out = new FileOutputStream(new File(uploadedFileLocation));
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    @GET
    @Path("/{imageId}")
    @Produces("image/jpeg")
    public Response getImage(@PathParam("imageId") String imageId
    ) throws IOException {

        return Response.ok(extractBytes(imageId)).build();
    }


    public byte[] extractBytes (String ImageName) throws IOException {
        // open image
        File imgPath = new File(ImageName);
        BufferedImage bufferedImage = ImageIO.read(imgPath);

        // get DataBufferBytes from Raster
        WritableRaster raster = bufferedImage .getRaster();
        DataBufferByte data   = (DataBufferByte) raster.getDataBuffer();

        return ( data.getData() );
    }

    @GET
    @Path("/{imageId}/{width}x{height}")
    @Produces("image/jpeg")
    public Response getImage(@PathParam("imageId") String imageId,
                             @PathParam("width") String pWidth,
                             @PathParam("height") String pHeight
    ) throws IOException {

        byte[] buf = extractBytes(imageId);

        int width = Integer.parseInt(pWidth);

        int height = Integer.parseInt(pHeight);

        ByteArrayInputStream bais = new ByteArrayInputStream(buf);

        BufferedImage img = ImageIO.read(bais);

        //the following size calculation ensures that the picture is resized to fit in the defined height/width boundaries whilst still maintaining the proportions
        if(img.getHeight()/height > img.getWidth() / width) {
            width = (int) 1.0/img.getHeight()*height*img.getWidth();
        }
        else {
            height = (int) 1.0/img.getWidth()*width*img.getHeight();
        }

        java.awt.Image scaledImage = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
        BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        imageBuff.getGraphics().drawImage(scaledImage, 0, 0, new Color(0,0,0), null);

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        ImageIO.write(imageBuff, "jpg", buffer);

        return Response.ok(buffer.toByteArray()).build();
    }

}



