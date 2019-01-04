package com.yuntian.aspectjplugin

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import com.yuntian.aspectjplugin.extension.AspectjExtension
import org.aspectj.bridge.IMessage
import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile

class AspectjPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        project.extensions.add("aspectjExtension", AspectjExtension)

        final def log = project.logger

        def isApp = project.plugins.withType(AppPlugin)
        def isLib = project.plugins.withType(LibraryPlugin)
        if (isApp) {
            log.error "App AspectjPlugin开始修改Class!";
        }
        if (isLib) {
            log.error "Library AspectjPlugin开始修改Class!";
        }
        if (!isApp && !isLib) {
            throw new IllegalStateException("'android' or 'android-library' plugin required.")
        }

        final def variants
        if (isApp) {
            variants = project.android.applicationVariants
        } else {
            variants = project.android.libraryVariants
        }

        variants.all { variant ->
            log.error("aspectjExtension : " + project.aspectjExtension.isEnable);
            if (!project.aspectjExtension.isEnable) {
                log.error "==========AspectjPlugin关闭=============="
                return;
            }

            JavaCompile javaCompile = variant.javaCompile
            javaCompile.doLast {
                log.error "==========javaCompile.doLast=========="
                String[] args = ["-showWeaveInfo",
                                 "-1.8",
                                 "-inpath", javaCompile.destinationDir.toString(), // class文件路径
                                 "-aspectpath", javaCompile.classpath.asPath, // 依赖包jar,arr路径
                                 "-d", javaCompile.destinationDir.toString(), // class文件路径
                                 "-classpath", javaCompile.classpath.asPath, // 依赖包jar,arr路径
                                 "-bootclasspath", project.android.bootClasspath.join(File.pathSeparator)]

                MessageHandler handler = new MessageHandler(true);
                new Main().run(args, handler);
                for (IMessage message : handler.getMessages(null, true)) {
                    switch (message.getKind()) {
                        case IMessage.ABORT:
                        case IMessage.ERROR:
                        case IMessage.FAIL:
                            log.error message.message, message.thrown
                            break;
                        case IMessage.WARNING:
                            log.warn message.message, message.thrown
                            break;
                        case IMessage.INFO:
                            log.info message.message, message.thrown
                            break;
                        case IMessage.DEBUG:
                            log.debug message.message, message.thrown
                            break;
                    }
                }
            }
        }

    }
}

