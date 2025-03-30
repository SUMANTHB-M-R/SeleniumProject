println "🚀 Debug: Script execution started!"
println "🔍 Debug: Checking workspace environment..."
println "📝 Debug: Preparing to locate the report file..."


println "📂 Debug: Jenkins Workspace -> ${workspace}"
println "📄 Debug: Expected Report Path -> ${reportPath.replace('/', '\\')}"

println "✅ Debug: Script execution completed!"
return "✔ Script executed successfully without reading the file."
