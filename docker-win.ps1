$path = $(convert-path .) -replace ':',''
$path = '\\' + $path + ':/home/klee/project'
docker run -it -v $($path) marvinlwenzel/soe2-kleeplus bash
