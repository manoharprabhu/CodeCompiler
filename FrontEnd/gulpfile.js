var gulp = require("gulp");
var usemin = require("gulp-usemin");
var uglify = require("gulp-uglify");
var minifyCss = require("gulp-minify-css");
var connect = require('gulp-connect');

gulp.task("default", ["copyfonts", "copyacethemes", "minify", "watch", "connect"]);

gulp.task("minify", function(){
    gulp.src("./src/*.html")
    .pipe(usemin({
        assetsDir: "src",
        css: [minifyCss, "concat"],
        js: [uglify, "concat"]
    }))
    .pipe(gulp.dest("public"))
    .pipe(connect.reload());
});

gulp.task('connect', function() {
    connect.server({
        root: 'public',
        livereload: true
    });
});

gulp.task("copyfonts", function(){
    gulp.src("src/bower_components/bootstrap/fonts/**/*.{ttf,woff,eof,svg}")
        .pipe(gulp.dest("public/fonts"));
});

gulp.task("copyacethemes", function(){
    gulp.src("src/bower_components/ace-builds/src-min/**/*.js")
        .pipe(gulp.dest("public/js"));
});

gulp.task("watch", function() {
	gulp.watch(["src/js/*.js", "src/*.html", "src/css/*.css"], ["minify"]);
});
