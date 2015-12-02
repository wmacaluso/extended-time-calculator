using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Diagnostics;

namespace ExecutableWrapper
{
    class Program
    {
        static void Main(string[] args)
        {
            string pathToJar = "\"Extended Time Calculator-jfx.jar\"";
            string exeDirName = "";

            try{
                exeDirName = System.IO.Path.GetDirectoryName(System.Reflection.Assembly.GetExecutingAssembly().Location);
            }
            catch (Exception ex) { }

            Process proc = new Process();
            ProcessStartInfo psinfo = new ProcessStartInfo();
            //psinfo.FileName = @"C:\Program Files\Java\jre1.8.0_25\bin\java";
            psinfo.FileName = exeDirName + @"\runtime\jre\bin\java.exe";
            psinfo.Arguments = "-jar " + pathToJar;
            psinfo.WorkingDirectory = exeDirName + "\\app";
            psinfo.UseShellExecute = false;
            psinfo.CreateNoWindow = true;
            proc.StartInfo = psinfo;
            proc.Start();
        }
    }
}
