package com.ds.runners;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Optional;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.ds.entity.MarriageSeeker;
import com.ds.service.IMarriageSeekerMgmtService;

@Component
public class LobTestRunner implements CommandLineRunner {
	@Autowired
	private IMarriageSeekerMgmtService service;

	@Override
	public void run(String... args) throws Exception {
		/*Scanner sc = new Scanner(System.in);
		//gathering mname
		System.out.println("enter marriageseeker Name:");
		String Name=sc.next();
		//gather mAddrs
		System.out.println("marriage seeker adddress:");
		String addrs=sc.next();
		//wether indian or not
		System.out.println("marriageseeker is indian or not:");
		boolean isIndian=sc.nextBoolean();
		//gathering photo
		System.out.println("enter photo path:");
		String photoPath=sc.next();
		//gathering Location of cvv
		System.out.println("entert resume path:");
		String resumePath=sc.next();
		//creating byte[] locating the file of the filesystem
		File file=new File(photoPath);
		FileInputStream fis=new FileInputStream(file);
		byte[] photoContent = new byte[fis.available()];
		photoContent = fis.readAllBytes();
		
		//create char[] locating the path of file if the file system
		file=new File(resumePath);
		FileReader reader = new FileReader(file);
		char[] resumeContent=new char[(int)file.length()];
		reader.read(resumeContent);
		
		
		//create entity class object
		MarriageSeeker seeker = new MarriageSeeker(Name,addrs,isIndian,photoContent,resumeContent);
		
			String resultMsg =service.registerMarriageSeeker(seeker);
			System.out.println("result msg is :"+resultMsg);
			
		fis.close();
		sc.close();
			reader.close();
				*/
			
		
			// logic to retrive LOBS
			Scanner sc = new Scanner(System.in);
			System.out.println("Enter marriageseeker id:");
			int msId = sc.nextInt();
		
			// invoke service method
			Optional<MarriageSeeker> opt = service.getMarriageSeekerInfo(msId);
			if (opt.isPresent()) {
				// get entity cls obj
				MarriageSeeker seeker = opt.get();
				System.out.println(seeker.getSid() + "...." + seeker.getName() + "..." + seeker.getAddrs() + "..."
						+ seeker.getIndian());
				// read the photo content(byte[])from entity object and write to a file
				byte[] photoContent = seeker.getPhoto();
				try (FileOutputStream fos = new FileOutputStream("photo_retrive.jpeg")) {
					fos.write(photoContent);
					fos.flush();
					System.out.println("photo retrive to file");
				} catch (Exception e) {
					e.printStackTrace();
				}
		
				// read the resume content(char[])
				// file as module
		
				char[] resumeContent = seeker.getResume();
				try (FileWriter writer = new FileWriter("resume_retrive.txt")) {
					writer.write(resumeContent);
					writer.flush();
		
					System.out.println("resume is retrived to file:");
		
				}
		
			} else {
				System.out.println("Record not found:");
			}
			sc.close();// close scanner
		
	}

}
