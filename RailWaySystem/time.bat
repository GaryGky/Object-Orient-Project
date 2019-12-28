for %%i in (input\*) do (
	echo %%i >> outF.txt
	java -cp E:\OO\Project\Project11\out\production\Project11 -Djava.ext.dirs=E:\OO\Project\lib Main < %%i >> outF.txt
)

java -cp E:\OO\Project\Project11\out\production\Project11 -Djava.ext.dirs=E:\OO\Project\lib Main < input/input2.txt >> outF.txt