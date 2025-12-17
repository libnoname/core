import {
	build as buildElectron,
	Platform,
	Arch,
	type PackagerOptions,
	type Configuration,
} from "electron-builder";
import { build as buildVite } from "vite";

async function main(
	targets: PackagerOptions["targets"],
	config: Partial<Configuration> = {}
) {
	const appPaths = await buildElectron({
		config: {
			asar: false,
			appId: "com.libnoname.noname",
			productName: "noname",
			directories: {
				output: "output",
			},
			files: [
				{ from: "dist", to: "" },
				{
					from: "../core/dist",
					to: "",
					filter: ["**/*", "!./package.json"],
				},
				{ from: "../core/dist/node_modules", to: "node_modules" },
				"package.json",
			],
			extraMetadata: {
				main: "app/main.js",
			},
			...config,
		},
		targets,
	});
	console.log("打包完成");
}

await buildVite();

switch (process.argv[2]) {
	case "win64":
		main(Platform.WINDOWS.createTarget("dir", Arch.x64), {
			win: {
				signAndEditExecutable: false,
				verifyUpdateCodeSignature: false,
				icon: "noname.ico",
			},
		});
		break;
	case "win32":
		main(Platform.WINDOWS.createTarget("dir", Arch.ia32), {
			win: {
				signAndEditExecutable: false,
				verifyUpdateCodeSignature: false,
				icon: "noname.ico",
			},
		});
		break;
	case "linux":
		main(Platform.LINUX.createTarget("dir", Arch.x64));
		break;
	case "macos":
		main(Platform.MAC.createTarget("dir", Arch.arm64), {
			mac: {
				identity: null,
			},
		});
		break;
	case "macos_intel":
		main(Platform.MAC.createTarget("dir", Arch.x64), {
			mac: {
				identity: null,
			},
		});
		break;
	default:
		console.log("未知平台:", process.argv[2]);
}
