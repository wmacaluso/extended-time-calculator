using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Diagnostics;

namespace RunJar
{
    class MainClass
    {
        public static void Main()
        {
            Process proc = new Process();
            ProcessStartInfo psinfo = new ProcessStartInfo();
            psinfo.FileName = "java";
            psinfo.Arguments = "-jar \"C:\\Users\\212336351\\Documents\\Code\\test\\TestTimer\\extended-time-calculator\\target\\jfx\\native\\Extended Time Calculator-jfx.jar\"";
            psinfo.UseShellExecute = false;
            psinfo.CreateNoWindow = true;
            proc.StartInfo = psinfo;
            proc.Start();

        }
    }
}

