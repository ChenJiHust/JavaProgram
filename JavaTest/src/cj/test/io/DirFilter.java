package cj.test.io;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

class DirFilter implements FilenameFilter{
	
	private Pattern pattern;
	public DirFilter(String regex){
		pattern = Pattern.compile(regex);
	}

	@Override
	public boolean accept(File dir, String name) {
		// TODO Auto-generated method stub
		return pattern.matcher(new File(name).getName()).matches();
	}
	
}
