package br.com.global5.infra.bean;

import br.com.global5.infra.util.AppUtils;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by zielinski on 10/06/17.
 */
public class DynamicImage extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            // Get image file.
            String file = AppUtils.imageName(request.getParameter("file"),false);
            
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(AppUtils.dirImagens + file));
            // Get image contents.
            byte[] bytes = new byte[in.available()];
            in.read(bytes);
            in.close();
            // Write image contents to response.
            response.getOutputStream().write(bytes);
        } catch (Exception e) {
            System.out.println("Imagem não encontrada... " );
        }
    }
    
    
    
    
}
