package cn.wendong.core;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Stack;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 外部class文件加载到JVM
 * 
 * @author MB yangtdo@qq.com
 * @date 2018-12-30
 */
public class ClassScanner {

	/**
	 *   * 从本地磁盘的某个路径上加载类， 如果是class文件：   * filePath路径应该为class文件包名的上一级.   *  
	 */
	public static List<Class<?>> LoadClass(String filePath, ClassLoader beanClassLoader) {
		List<Class<?>> classNameList = new ArrayList<>();
		File clazzPath = new File(filePath);
		int clazzCount = 0;
		if (clazzPath.exists() && clazzPath.isDirectory()) {
			int clazzPathLen = clazzPath.getAbsolutePath().length() + 1;
			Stack<File> stack = new Stack<>();
			stack.push(clazzPath);
			while (stack.isEmpty() == false) {
				File path = stack.pop();
				File[] classFiles = path.listFiles(new FileFilter() {
					public boolean accept(File pathname) {
						return pathname.isDirectory() || pathname.getName().endsWith(".class");
					}
				});
				for (File subFile : classFiles) {
					if (subFile.isDirectory()) {
						stack.push(subFile);
					} else {
						if (clazzCount++ == 0) {
							Method method = null;
							try {
								method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
							} catch (NoSuchMethodException e) {
								e.printStackTrace();
							}
							boolean accessible = method.isAccessible();
							try {
								if (accessible == false) {
									method.setAccessible(true);
								}
								URLClassLoader classLoader = (URLClassLoader) beanClassLoader;
								try {
									URL url = clazzPath.toURI().toURL();
									method.invoke(classLoader, url);
								} catch (IllegalAccessException e) {
									e.printStackTrace();
								} catch (InvocationTargetException e) {
									e.printStackTrace();
								} catch (MalformedURLException e) {
									e.printStackTrace();
								}
							} finally {
								method.setAccessible(accessible);
							}
						}
						String className = subFile.getAbsolutePath();
						className = className.substring(clazzPathLen, className.length() - 6);
						className = className.replace(File.separatorChar, '.');
						try {
							classNameList.add(Class.forName(className));
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return classNameList;
	}

	/**
	 *   从本地磁盘的某个路径上加载类，
	 *   如果是class文件 filePath路径应该为class文件包名的上一级.
	 *   如果是jar包：  则是jar包所在目录.
	 */
	public static List<Class<?>> loadClass(String filePath, ClassLoader beanClassLoader) {
		List<Class<?>> classList = new ArrayList<>();
		File file = new File(filePath);
		if (file.exists() && file.isDirectory()) {
			Stack<File> stack = new Stack<>();
			stack.push(file);
			while (!stack.isEmpty()) {
				File path = stack.pop();
				// 只需要class文件或者jar包
				File[] classFiles = path.listFiles(new FileFilter() {
					public boolean accept(File pathname) {
						return pathname.isDirectory() || pathname.getName().endsWith(".class")
								|| pathname.getName().endsWith(".jar");
					}
				});
				for (File subFile : classFiles) {
					if (subFile.isDirectory()) {
						// 如果是目录，则加入栈中
						stack.push(subFile);
					} else {
						URL url = null;
						JarFile jar = null;
						Method method = null;
						Boolean accessible = null;
						String className = subFile.getAbsolutePath();
						try {
							// 反射并调用URLClassLoader的addURL方法
							URLClassLoader classLoader = (URLClassLoader) beanClassLoader;
							method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
							accessible = method.isAccessible();
							if (accessible == false) {
								method.setAccessible(true);
							}
							if (className.endsWith(".class")) {
								// 一次性加载目录下的所有class文件
								// 这里一定不要写成url = subFile.toURI().toURL();
								url = file.toURI().toURL();
								method.invoke(classLoader, url);
								// 拼类名，并进行类型加载
								int clazzPathLen = file.getAbsolutePath().length() + 1;
								className = className.substring(clazzPathLen, className.length() - 6);
								className = className.replace(File.separatorChar, '.');
								classList.add(classLoader.loadClass(className));
							} else if (className.endsWith(".jar")) {
								// 如果是jar包，加载该jar包
								url = subFile.toURI().toURL();
								method.invoke(classLoader, url);
								// 获取jar
								jar = new JarFile(new File(className));
								// 从此jar包 得到一个枚举类
								Enumeration<JarEntry> entries = jar.entries();
								// 同样的进行循环迭代
								while (entries.hasMoreElements()) {
									// 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件
									// 如META-INF等文件
									JarEntry entry = entries.nextElement();
									String name = entry.getName();
									// 如果是以/开头的
									if (name.charAt(0) == '/') {
										// 获取后面的字符串
										name = name.substring(1);
									}
									name = name.replace(File.separatorChar, '.');
									// 获取class文件
									if (name.endsWith(".class") && !entry.isDirectory()) {
										String classNames = name.substring(0, name.length() - 6);
										// 添加到classes
										classList.add(classLoader.loadClass(classNames));
									}
								}

							}

						} catch (Exception e) {
							throw new RuntimeException(e.getMessage());
						} finally {
							if (null != jar) {
								try {
									jar.close();
								} catch (IOException e) {
									throw new RuntimeException(e.getMessage());
								}
							}
							if (null != method && null != accessible) {
								method.setAccessible(accessible);
							}
						}
						///////////////
					}
				}
			}
		}
		return classList;
	}

}
