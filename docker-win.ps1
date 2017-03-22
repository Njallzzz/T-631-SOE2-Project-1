$path = $(convert-path .) -replace ':',''
$path = '\\' + $path + ':/home/klee'
docker run -it -v $($path) marvinlwenzel/soe2-kleeplus bash