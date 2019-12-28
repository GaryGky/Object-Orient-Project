for %%i in (P13_test\*) do (
	java -cp E:\OO\Project\Project14\out\production\Project14 -Djava.ext.dirs=E:\OO\Project\lib Main < %%i >> outF
)